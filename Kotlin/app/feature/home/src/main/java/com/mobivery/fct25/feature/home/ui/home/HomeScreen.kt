package com.mobivery.fct25.feature.home.ui.home

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.designsystem.component.buttons.SecondaryButton
import com.mobivery.fct25.app.common.designsystem.component.permissions.PrimaryButtonWithPermission
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXL
import com.mobivery.fct25.app.common.extension.getTempImageUri
import com.mobivery.fct25.feature.home.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModelInterface = hiltViewModel<HomeViewModel>(),
    goToLogin: () -> Unit,
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val uri = remember {
        if (!isPreview) context.getTempImageUri() else Uri.EMPTY
    }
    var capturedImageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = SPACING_S,
                    end = SPACING_S,
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(SPACING_XXL))
            Text(text = stringResource(id = R.string.home_title_text))
            Spacer(modifier = Modifier.height(SPACING_XXL))
            PrimaryButtonWithPermission(
                permission = Manifest.permission.CAMERA,
                rationaleMessage = stringResource(id = R.string.home_camera_rationale),
                permissionRejectedMessage = stringResource(id = R.string.home_camera_rejected_permission),
                buttonText = stringResource(id = R.string.home_attach_photo_button),
                actionWhenGranted = {
                    cameraLauncher.launch(uri)
                }
            )
            Spacer(modifier = Modifier.height(SPACING_XXL))
            PrimaryButtonWithPermission(
                permission = Manifest.permission.CAMERA,
                rationaleMessage = stringResource(id = R.string.home_camera_rationale),
                permissionRejectedMessage = stringResource(id = R.string.home_camera_rejected_permission),
                rejectedDialog = true,
                buttonText = stringResource(id = R.string.home_attach_photo_dialog_button),
                actionWhenGranted = {
                    cameraLauncher.launch(uri)
                }
            )
            Spacer(modifier = Modifier.height(SPACING_XXL))
            SecondaryButton(
                text = stringResource(id = R.string.home_logout_button),
                onClick = {
                    viewModel.handle(HomeEvent.Logout)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            viewModel = composePreviewViewModel,
            goToLogin = {}
        )
    }
}

private val composePreviewViewModel by lazy {
    object : HomeViewModelInterface {
        // Outputs
        override val homeUiState = MutableStateFlow(HomeUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun handle(event: HomeEvent) {}
        override fun closeError() {}
    }
}