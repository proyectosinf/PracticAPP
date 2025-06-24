package com.mobivery.fct25.feature.about.ui.about

import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.component.permissions.PrimaryButtonWithMultiplePermission
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXL
import com.mobivery.fct25.feature.about.R
import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun AboutScreen(
    viewModel: AboutViewModelInterface = hiltViewModel<AboutViewModel>(),
) {
    val uiState by viewModel.aboutUiState.collectAsStateWithLifecycle()
    val showPermissionGranted = remember { mutableStateOf(false) }

    if (showPermissionGranted.value) {
        InfoDialog(
            title = "",
            message = "Permiso concedido!",
            primaryButton = stringResource(id = R.string.common_accept_text),
            onPrimaryButtonClick = {
                showPermissionGranted.value = false
            },
            onSecondaryButtonClick = {}
        )
    }

    Scaffold { padding ->
        Column(
            Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(
                    top = SPACING_L,
                    start = SPACING_S,
                    end = SPACING_S
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(SPACING_XXL))
            Text(text = stringResource(id = R.string.about_title_text))
            Spacer(modifier = Modifier.height(SPACING_XXL))
            PrimaryButtonWithMultiplePermission(
                permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                rationaleMessage = stringResource(id = R.string.about_multiple_permissions_rationale),
                permissionsRejectedMessage = stringResource(id = R.string.about_multiple_permissions_rejected),
                rejectedDialog = true,
                buttonText = stringResource(id = R.string.about_button),
                actionWhenGranted = {
                    showPermissionGranted.value = true
                }
            )
        }
    }

}

@Preview
@Composable
private fun AboutScreenPreview() {
    AppTheme {
        AboutScreen(
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : AboutViewModelInterface {
        // Outputs
        override val aboutUiState = MutableStateFlow(AboutUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun handle(event: AboutEvent) {}
        override fun closeError() {}
    }
}