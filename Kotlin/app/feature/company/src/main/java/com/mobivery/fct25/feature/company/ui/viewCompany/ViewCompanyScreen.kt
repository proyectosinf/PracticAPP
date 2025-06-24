package com.mobivery.fct25.feature.company.ui.viewCompany

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mobivery.fct25.app.common.designsystem.component.dialogs.ErrorDialog
import com.mobivery.fct25.app.common.designsystem.component.dialogs.LogoutDialog
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithActionRight
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.company.R
import com.mobivery.fct25.feature.company.ui.viewCompany.ViewCompanyViewModel.ViewCompanyEvent

@Composable
fun ViewCompanyScreen(
    viewModel: ViewCompanyViewModelInterface = hiltViewModel<ViewCompanyViewModel>()
) {
    val uiState by viewModel.viewCompanyUiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.handle(ViewCompanyEvent.OnLogoSelected(it)) }
    }

    LaunchedEffect(uiState.shouldOpenImagePicker) {
        if (uiState.shouldOpenImagePicker) {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
            viewModel.handle(ViewCompanyEvent.OnImagePickerOpened)
        }
    }

    Scaffold(
        containerColor = AppColors.surface,
        topBar = {
            TopBarWithActionRight(
                title = stringResource(R.string.company_view_company_title_text),
                centerTitle = true,
                onRightActionClick = { viewModel.handle(ViewCompanyEvent.RequestLogoutConfirmation) }
            )
        }
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .background(AppColors.surface)
        ) {
            Loader(viewModel = viewModel)

            if (uiState.showLogoutConfirmationDialog) {
                LogoutDialog(
                    title = stringResource(R.string.company_logout_text_text),
                    message = stringResource(R.string.company_areyousure_text),
                    onConfirmLogout = { viewModel.handle(ViewCompanyEvent.ConfirmLogout) },
                    onCancel = { viewModel.handle(ViewCompanyEvent.CancelLogout) },
                    onDismiss = { viewModel.handle(ViewCompanyEvent.CancelLogout) }
                )
            }

            if (uiState.showErrorDialog) {
                ErrorDialog(
                    title = stringResource(R.string.company_register_failure_title_text),
                    message = uiState.errorMessageRes?.let { stringResource(it) } ?: "",
                    onButtonClick = { viewModel.handle(ViewCompanyEvent.OnErrorDialogDismissed) },
                    onDismiss = { viewModel.handle(ViewCompanyEvent.OnErrorDialogDismissed) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SPACING_M, vertical = SPACING_S)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = SPACING_M)
                        .clickable { viewModel.handle(ViewCompanyEvent.OnChangeLogoRequested) }
                ) {
                    AsyncImage(
                        model = uiState.logoInput.text,
                        contentDescription = stringResource(R.string.company_logo_content_description_text),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp,
                    color = AppColors.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(SPACING_M)) {
                        Text(
                            text = stringResource(R.string.company_name_label_text),
                            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.onSurfaceSecondary
                        )
                        Spacer(modifier = Modifier.height(SPACING_S))
                        Text(
                            text = uiState.nameInput.text,
                            style = AppTypography.bodyLarge,
                            color = AppColors.onSurface
                        )

                        Spacer(modifier = Modifier.height(SPACING_M))

                        Text(
                            text = stringResource(R.string.company_sector_label_text),
                            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.onSurfaceSecondary
                        )
                        Spacer(modifier = Modifier.height(SPACING_S))
                        Text(
                            text = uiState.sectorInput.text,
                            style = AppTypography.bodyLarge,
                            color = AppColors.onSurface
                        )

                        if (uiState.webInput.text.isNotBlank()) {
                            Spacer(modifier = Modifier.height(SPACING_M))
                            Text(
                                text = stringResource(R.string.company_web_label_text),
                                style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AppColors.onSurfaceSecondary
                            )
                            Spacer(modifier = Modifier.height(SPACING_S))
                            Text(
                                text = uiState.webInput.text,
                                style = AppTypography.bodyLarge,
                                color = AppColors.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(SPACING_M))

                        Text(
                            text = stringResource(R.string.company_cif_label_text),
                            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppColors.onSurfaceSecondary
                        )
                        Spacer(modifier = Modifier.height(SPACING_S))
                        Text(
                            text = uiState.cifInput.text,
                            style = AppTypography.bodyLarge,
                            color = AppColors.onSurface
                        )
                    }
                }
            }
        }
    }
}
