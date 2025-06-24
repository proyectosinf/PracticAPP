package com.mobivery.fct25.app.common.designsystem.component.permissions

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.extension.openAppSettings
import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 *  - Reference -
 *
 *  Android permission request workflow:
 *      https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
 *
 *  And the Jetpack Compose permissions library:
 *      https://google.github.io/accompanist/permissions/
 */

/**
 *  Primary button with logic to manage multiple permission request
 *
 *  @permissions: list of permissions ("Manifest.permission.CAMERA")
 *  @rationaleMessage: message when rejected for first time
 *  @permissionsRejectedMessage: message when rejected forever
 *  @rejectedDialog: to show a dialog when a permission is rejected instead of a Text over the button
 *  @actionWhenGranted: use this instead of your button's onClick!
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrimaryButtonWithMultiplePermission(
    permissions: List<String>,
    rationaleMessage: String,
    permissionsRejectedMessage: String,
    rejectedDialog: Boolean = false,
    buttonText: String,
    actionWhenGranted: () -> Unit,
) {
    val permissionRequested = rememberSaveable { mutableStateOf(false) }
    val permissionState = if (LocalInspectionMode.current) {
        // Just for preview:
        MultiplePermissionStatePreview()
    } else {
        // In the app:
        rememberMultiplePermissionsState(permissions) {
            permissionRequested.value = true
        }
    }
    val canShowRejectedDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var permissionRejectedForever = false
    val rejectedMessageToShow = when {
        !permissionState.allPermissionsGranted && permissionState.shouldShowRationale -> {
            // Case permission was rejected once, there is one  more try
            rationaleMessage
        }
        !permissionState.allPermissionsGranted && permissionRequested.value -> {
            // Case permission was rejected twice
            // Permission dialog will not be shown again
            // User needs to go to app settings to enable it.
            permissionRejectedForever = true
            permissionsRejectedMessage
        }
        else -> null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (rejectedMessageToShow != null) {
            if (rejectedDialog) {
                if (canShowRejectedDialog.value) {
                    if (permissionRejectedForever) {
                        InfoDialog(
                            title = stringResource(id = R.string.common_error_text),
                            message = rejectedMessageToShow,
                            primaryButton = stringResource(id = R.string.common_accept_text),
                            secondaryButton = stringResource(id = R.string.common_open_app_settings_text),
                            onPrimaryButtonClick = {
                                canShowRejectedDialog.value = false
                            },
                            onSecondaryButtonClick = {
                                if (permissionRejectedForever) {
                                    context.openAppSettings()
                                }
                                canShowRejectedDialog.value = false
                            },
                        )
                    } else {
                        InfoDialog(
                            title = "",
                            message = rejectedMessageToShow,
                            primaryButton = stringResource(id = R.string.common_accept_text),
                            onPrimaryButtonClick = {
                                canShowRejectedDialog.value = false
                            },
                            onSecondaryButtonClick = {},
                        )
                    }
                }
            } else {
                RejectedPermissionText(
                    rejectedPermissionString = rejectedMessageToShow,
                    isRejectedForever = permissionRejectedForever
                )
            }
        }
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = buttonText,
            onClick = {
                canShowRejectedDialog.value = true
                permissionState.launchMultiplePermissionRequest()
                if (permissionState.allPermissionsGranted) {
                    actionWhenGranted()
                }
            },
        )
    }
}

@ExperimentalPermissionsApi
class MultiplePermissionStatePreview : MultiplePermissionsState {
    override val allPermissionsGranted: Boolean
        get() = false
    override val permissions: List<PermissionState>
        get() = emptyList()
    override val revokedPermissions: List<PermissionState>
        get() = emptyList()
    override val shouldShowRationale: Boolean
        get() = true

    override fun launchMultiplePermissionRequest() {
        // Do nothing
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrimaryButtonWithMultiplePermissionPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = SPACING_S),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PrimaryButtonWithMultiplePermission(
            permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            rationaleMessage = "Necesitamos que aceptes el permiso para abrir la cámara",
            permissionsRejectedMessage = "La app no tiene permiso para abrir la camara, ve a los settings de la app y acéptalos",
            buttonText = "Abrir cámara",
            actionWhenGranted = {},
        )
        Spacer(modifier = Modifier.height(SPACING_XL))
        PrimaryButtonWithMultiplePermission(
            permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            rationaleMessage = "Necesitamos que aceptes el permiso para abrir la cámara",
            permissionsRejectedMessage = "La app no tiene permiso para abrir la camara, ve a los settings de la app y acéptalos",
            rejectedDialog = true,
            buttonText = "Abrir cámara con diálogo de permisos",
            actionWhenGranted = {},
        )
    }
}
