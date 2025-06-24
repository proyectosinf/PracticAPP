package com.mobivery.fct25.feature.company.ui.createcompany

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.ErrorDialog
import com.mobivery.fct25.app.common.designsystem.component.dialogs.LogoutDialog
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.MandatoryTextField
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithActionRight
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.feature.company.R
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.CifLostFocus
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.CloseErrorDialog
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.DidNavigate
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.NameLostFocus
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.OnCifChanged
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.OnNameChanged
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.OnSectorChanged
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.OnWebChanged
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.SectorLostFocus
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.Submit
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyEvent.WebLostFocus
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyViewModel.CreateCompanyNavigation
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.VIEW_COMPANY_ROUTE
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CreateCompanyScreen(
    navController: NavHostController,
    viewModel: CreateCompanyViewModelInterface = hiltViewModel<CreateCompanyViewModel>()
) {
    val uiState by viewModel.createCompanyUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (uiState.navigation) {
            is CreateCompanyNavigation.NavigateToCompany -> {
                navController.navigate(VIEW_COMPANY_ROUTE) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }

            null -> Unit
        }

        if (uiState.navigation != null) {
            viewModel.handle(DidNavigate)
        }
    }

    Scaffold(
        containerColor = AppColors.surface,
        topBar = {
            TopBarWithActionRight(
                onRightActionClick = { viewModel.handle(CreateCompanyEvent.OnLogoutButtonClick) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppColors.surface)
                .imePadding()
        ) {
            Loader(viewModel = viewModel)

            if (uiState.showLogoutConfirmationDialog) {
                LogoutDialog(
                    title = stringResource(R.string.logout_confirmation_title_text),
                    message = stringResource(R.string.logout_confirmation_message_text),
                    onConfirmLogout = { viewModel.handle(CreateCompanyEvent.ConfirmLogout) },
                    onCancel = { viewModel.handle(CreateCompanyEvent.CancelLogout) },
                    onDismiss = { viewModel.handle(CreateCompanyEvent.CancelLogout) }
                )
            }

            if (uiState.showErrorDialog) {
                ErrorDialog(
                    title = uiState.errorDialogTitle
                        ?: stringResource(R.string.company_register_failure_title_text),
                    message = uiState.errorDialogMessage
                        ?: stringResource(R.string.company_register_failure_message_text),
                    onButtonClick = { viewModel.handle(CloseErrorDialog) },
                    onDismiss = { viewModel.handle(CloseErrorDialog) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SPACING_M, vertical = SPACING_S)
            ) {
                Text(
                    text = stringResource(R.string.company_welcome_message_text),
                    style = AppTypography.titleLarge,
                    color = AppColors.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SPACING_M),
                    textAlign = TextAlign.Center
                )

                MandatoryTextField(
                    value = uiState.nameInput.text,
                    onValueChange = { viewModel.handle(OnNameChanged(it)) },
                    baseLabel = stringResource(R.string.company_name_label_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.nameInput.errorType != null,
                    errorMessage = uiState.nameInput.errorType?.messageResId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(NameLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                MandatoryTextField(
                    value = uiState.cifInput.text,
                    onValueChange = { viewModel.handle(OnCifChanged(it)) },
                    baseLabel = stringResource(R.string.company_cif_label_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.cifInput.errorType != null,
                    errorMessage = uiState.cifInput.errorType?.messageResId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CifLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                MandatoryTextField(
                    value = uiState.sectorInput.text,
                    onValueChange = { viewModel.handle(OnSectorChanged(it)) },
                    baseLabel = stringResource(R.string.company_sector_label_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.sectorInput.errorType != null,
                    errorMessage = uiState.sectorInput.errorType?.messageResId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(SectorLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                CustomTextField(
                    value = uiState.webInput.text,
                    onValueChange = { viewModel.handle(OnWebChanged(it)) },
                    label = stringResource(R.string.company_web_label_text),
                    isError = uiState.webInput.errorType != null,
                    errorMessage = uiState.webInput.errorType?.messageResId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(WebLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_M))

                PrimaryButton(
                    text = stringResource(R.string.company_create_button),
                    onClick = { viewModel.handle(Submit) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateCompanyScreenPreview() {
    AppTheme {
        val navController = rememberNavController()
        CreateCompanyScreen(
            navController = navController,
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : CreateCompanyViewModelInterface {
        override val createCompanyUiState = MutableStateFlow(
            CreateCompanyViewModel.CreateCompanyUiState(
                nameInput = TextFieldUiModel("Mobivery"),
                cifInput = TextFieldUiModel("A12345678"),
                sectorInput = TextFieldUiModel("Software"),
                webInput = TextFieldUiModel("https://mobivery.com")
            )
        )
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow<AppError?>(null)
        override fun closeError() {}
        override fun handle(event: CreateCompanyEvent) {}
    }
}
