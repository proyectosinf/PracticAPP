package com.mobivery.fct25

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.ui.main.MainCompose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : FragmentActivity() {

    private val viewModel: RootViewModel by viewModels()

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    private var updateAppFlowResultLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    private var appUpdateManager: AppUpdateManager? = null

    private val updateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // The update is downloaded.
            viewModel.handle(RootEvent.InAppUpdateDownloaded)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Edge to edge content is by default in API >= 35, this enables it for older APIs
         * https://developer.android.com/develop/ui/compose/layouts/insets
         *
         *     setContent {
         *         Box(Modifier.safeDrawingPadding()) {
         *             // the rest of the app
         *         }
         *     }
         *
         * This snippet applies the safeDrawing window insets as padding around the entire
         * content of the app. While this ensures that interactable elements don't overlap
         * with the system UI, it also means that none of the app will draw behind the
         * system UI to achieve an edge-to-edge effect. To make full use of the entire window,
         * you need to fine-tune where the insets are applied on a screen-by-screen or
         * component-by-component basis.
         */
        enableEdgeToEdge()
        configureInAppUpdateResult()
        setContent {
            val uiState by viewModel.rootUiState.collectAsStateWithLifecycle()

            AppTheme {
                LaunchedEffect(uiState.navigation) {
                    when (val navigation = uiState.navigation) {
                        is RootNavigation.ExitApp -> {
                            finish()
                        }

                        else -> {
                            // Does nothing
                        }
                    }
                }

                LaunchedEffect(uiState.updateApp) {
                    if (uiState.updateApp) {
                        startInAppUpdate(uiState.forceUpdate)
                    }
                }

                LaunchedEffect(uiState.installDownloadedUpdate) {
                    if (uiState.installDownloadedUpdate) {
                        appUpdateManager?.completeUpdate()
                    }
                }

                when (uiState.dialog) {
                    RootDialog.RetryUpdateDialog -> {
                        InfoDialog(
                            title = stringResource(R.string.common_update_app_text),
                            message = stringResource(R.string.common_default_server_error_text),
                            primaryButton = stringResource(R.string.common_update_button),
                            onPrimaryButtonClick = {
                                startInAppUpdate(uiState.forceUpdate)
                            },
                            secondaryButton = stringResource(R.string.common_cancel_text),
                            onSecondaryButtonClick = {
                                viewModel.handle(RootEvent.DismissUpdateDialog)
                            }
                        )
                    }

                    RootDialog.UpdateDownloadedDialog -> {
                        InfoDialog(
                            title = stringResource(R.string.common_update_app_text),
                            message = stringResource(R.string.common_optional_update_text),
                            primaryButton = stringResource(R.string.common_update_button),
                            onPrimaryButtonClick = {
                                viewModel.handle(RootEvent.InAppUpdateInstall)
                            },
                            secondaryButton = stringResource(R.string.common_cancel_text),
                            onSecondaryButtonClick = {
                                viewModel.handle(RootEvent.DismissDialog)
                            }
                        )
                    }

                    else -> { /* Do Nothing*/
                    }
                }

                MainCompose(onExitApp = {
                    finish()
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            ?.appUpdateInfo
            ?.addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    viewModel.handle(RootEvent.InAppUpdateDownloaded)
                }
            }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) && ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher = registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    // Do nothing
                }
                requestPermissionLauncher?.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun configureInAppUpdateResult() {
        updateAppFlowResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            when {
                result.resultCode == RESULT_CANCELED -> {
                    viewModel.handle(RootEvent.InAppUpdateCancelled)
                }

                result.resultCode != RESULT_OK -> {
                    viewModel.handle(RootEvent.InAppUpdateError)
                }
            }
        }
    }

    private fun startInAppUpdate(forceUpdate: Boolean) {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo

        val updateType = if (forceUpdate) {
            AppUpdateType.IMMEDIATE
        } else {

            // Before starting an update, register a listener for updates.
            appUpdateManager?.registerListener(updateListener)

            AppUpdateType.FLEXIBLE
        }

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    updateType
                )
            ) {
                updateAppFlowResultLauncher?.let {
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        it,
                        AppUpdateOptions.newBuilder(updateType).build()
                    )
                }
            }
        }
    }
}
