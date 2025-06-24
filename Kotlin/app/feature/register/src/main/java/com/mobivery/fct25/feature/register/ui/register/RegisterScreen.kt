package com.mobivery.fct25.feature.register.ui.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.ErrorDialog
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.MandatoryPasswordTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.MandatoryTextField
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithLeftCancel
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.register.R
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.CONFIRM_PASSWORD_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.EMAIL_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.PASSWORD_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterEvent
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterUiState
import com.mobivery.template.domain.model.user.UserRole

object RegisterTestTags {
    const val REGISTERBUTTON = "register_button"
    const val EMAIL_FIELD = "email_field"
    const val PASSWORD_FIELD = "password_field"
    const val CONFIRM_PASSWORD_FIELD = "confirm_password_field"
    const val NAME_FIELD = "name_field"
    const val SURNAME_FIELD = "surname_field"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModelInterface = hiltViewModel<RegisterViewModel>()
) {
    val uiState by viewModel.registerUiState.collectAsStateWithLifecycle()
    uiState.globalError?.let { error ->
        ErrorDialog(
            title = stringResource(R.string.common_error_text),
            message = stringResource(error.messageRes),
            onButtonClick = { viewModel.handle(RegisterEvent.ErrorDismissed) }
        )
    }

    val scrollState = rememberScrollState()

    BackHandler { onNavigateBack() }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarWithLeftCancel(
                containerColor = Color.Transparent,
                onCancelClick = onNavigateBack
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
        ) {
            Loader(viewModel = viewModel)
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                Surface(
                    color = AppColors.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SPACING_M, vertical = SPACING_S)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.practicapp),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(bottom = SPACING_L)
                        )

                        Text(
                            text = stringResource(id = R.string.register_statement_text),
                            style = AppTypography.headlineSmall,
                            color = AppColors.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = SPACING_S),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.register_sub_statement_text),
                            style = AppTypography.titleLarge,
                            color = AppColors.onSurfaceSecondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = SPACING_S),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Role Selection
                        Text(
                            text = stringResource(R.string.register_role_label_text) + " " +
                                    stringResource(R.string.common_mandatory_placeholder_text),
                            style = AppTypography.bodyLarge
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SPACING_M)
                        ) {
                            FilterChip(
                                selected = uiState.selectedRole == UserRole.STUDENT,
                                onClick = { viewModel.handle(RegisterEvent.OnRoleChanged(UserRole.STUDENT)) },
                                label = { Text(stringResource(R.string.register_role_student_text)) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = uiState.selectedRole == UserRole.TUTOR,
                                onClick = { viewModel.handle(RegisterEvent.OnRoleChanged(UserRole.TUTOR)) },
                                label = { Text(stringResource(R.string.register_role_tutor_text)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        uiState.roleInput.errorType?.let { error ->
                            Text(
                                text = stringResource(error.messageResourceId),
                                color = AppColors.error,
                                style = AppTypography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Email
                        MandatoryTextField(
                            modifier = Modifier.testTag(EMAIL_FIELD),
                            value = uiState.emailInput.text,
                            onValueChange = { viewModel.handle(RegisterEvent.OnEmailChanged(it)) },
                            baseLabel = stringResource(R.string.register_email_label_text),
                            mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                            isError = uiState.emailInput.errorType != null,
                            errorMessage = uiState.emailInput.errorType?.messageResourceId?.let {
                                stringResource(it)
                            } ?: "",
                            onFocusLost = { viewModel.handle(RegisterEvent.EmailLostFocus) }
                        )

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Password
                        MandatoryPasswordTextField(
                            modifier = Modifier.testTag(PASSWORD_FIELD),
                            value = uiState.passwordInput.text,
                            onValueChange = { viewModel.handle(RegisterEvent.OnPasswordChanged(it)) },
                            baseLabel = stringResource(R.string.register_password_label_text),
                            mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                            isError = uiState.passwordInput.errorType != null,
                            errorMessage = uiState.passwordInput.errorType?.messageResourceId?.let {
                                stringResource(it)
                            } ?: "",
                            onFocusLost = { viewModel.handle(RegisterEvent.PasswordLostFocus) }
                        )

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Confirm Password
                        MandatoryPasswordTextField(
                            modifier = Modifier.testTag(CONFIRM_PASSWORD_FIELD),
                            value = uiState.confirmPasswordInput.text,
                            onValueChange = {
                                viewModel.handle(
                                    RegisterEvent.OnConfirmPasswordChanged(
                                        it
                                    )
                                )
                            },
                            baseLabel = stringResource(R.string.register_confirm_password_label_text),
                            mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                            isError = uiState.confirmPasswordInput.errorType != null,
                            errorMessage = uiState.confirmPasswordInput.errorType?.messageResourceId?.let {
                                stringResource(it)
                            } ?: "",
                            onFocusLost = { viewModel.handle(RegisterEvent.ConfirmPasswordLostFocus) }
                        )

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Name
                        MandatoryTextField(
                            modifier = Modifier.testTag(RegisterTestTags.NAME_FIELD),
                            value = uiState.nameInput.text,
                            onValueChange = { viewModel.handle(RegisterEvent.OnNameChanged(it)) },
                            baseLabel = stringResource(R.string.register_name_label_text),
                            mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                            isError = uiState.nameInput.errorType != null,
                            errorMessage = uiState.nameInput.errorType?.messageResourceId?.let {
                                stringResource(it)
                            } ?: "",
                            onFocusLost = { viewModel.handle(RegisterEvent.NameLostFocus) })

                        Spacer(modifier = Modifier.height(SPACING_S))

                        // Surname
                        MandatoryTextField(
                            modifier = Modifier.testTag(RegisterTestTags.SURNAME_FIELD),
                            value = uiState.surnameInput.text,
                            onValueChange = { viewModel.handle(RegisterEvent.OnSurnameChanged(it)) },
                            baseLabel = stringResource(R.string.register_lastname_label_text),
                            mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                            isError = uiState.surnameInput.errorType != null,
                            errorMessage = uiState.surnameInput.errorType?.messageResourceId?.let {
                                stringResource(it)
                            } ?: "",
                            onFocusLost = { viewModel.handle(RegisterEvent.SurnameLostFocus) })

                        if (uiState.selectedRole == UserRole.STUDENT) {
                            StudentFields(uiState, viewModel)
                        }

                        Spacer(modifier = Modifier.height(SPACING_S))

                        PrimaryButton(
                            onClick = { viewModel.handle(RegisterEvent.RegisterClicked) },
                            text = stringResource(R.string.register_send_button_button),
                            enabled = !uiState.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(RegisterTestTags.REGISTERBUTTON)
                        )

                        Spacer(modifier = Modifier.height(SPACING_M))
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentFields(
    uiState: RegisterUiState, viewModel: RegisterViewModelInterface
) {
    // DNI

    Spacer(modifier = Modifier.height(SPACING_S))

    CustomTextField(
        value = uiState.dniInput.text,
        onValueChange = { viewModel.handle(RegisterEvent.OnDniChanged(it)) },
        label = stringResource(R.string.register_dni_label_text),
        isError = uiState.dniInput.errorType != null,
        errorMessage = uiState.dniInput.errorType?.messageResourceId?.let {
            stringResource(it)
        } ?: "",
        onFocusLost = { viewModel.handle(RegisterEvent.DniLostFocus) }
    )

    Spacer(modifier = Modifier.height(SPACING_S))

    // Social Security
    CustomTextField(
        value = uiState.socialSecurityInput.text,
        onValueChange = { viewModel.handle(RegisterEvent.OnSocialSecurityChanged(it)) },
        label = stringResource(R.string.register_social_security_label_text),
        isError = uiState.socialSecurityInput.errorType != null,
        errorMessage = uiState.socialSecurityInput.errorType?.messageResourceId?.let {
            stringResource(it)
        } ?: "",
        onFocusLost = { viewModel.handle(RegisterEvent.SocialSecurityLostFocus) }
    )

    Spacer(modifier = Modifier.height(SPACING_S))

    // Contact Name
    CustomTextField(
        value = uiState.contactNameInput.text,
        onValueChange = { viewModel.handle(RegisterEvent.OnContactNameChanged(it)) },
        label = stringResource(R.string.register_contact_name_label_text),
        isError = uiState.contactNameInput.errorType != null,
        errorMessage = uiState.contactNameInput.errorType?.messageResourceId?.let {
            stringResource(it)
        } ?: "",
        onFocusLost = { viewModel.handle(RegisterEvent.ContactNameLostFocus) }
    )

    Spacer(modifier = Modifier.height(SPACING_S))

    // Contact Email
    CustomTextField(
        value = uiState.contactEmailInput.text,
        onValueChange = { viewModel.handle(RegisterEvent.OnContactEmailChanged(it)) },
        label = stringResource(R.string.register_contact_email_label_text),
        isError = uiState.contactEmailInput.errorType != null,
        errorMessage = uiState.contactEmailInput.errorType?.messageResourceId?.let {
            stringResource(it)
        } ?: "",
        onFocusLost = { viewModel.handle(RegisterEvent.ContactEmailLostFocus) }
    )

    Spacer(modifier = Modifier.height(SPACING_S))

    // Contact Phone
    CustomTextField(
        value = uiState.contactPhoneInput.text,
        onValueChange = { viewModel.handle(RegisterEvent.OnContactPhoneChanged(it)) },
        label = stringResource(R.string.register_contact_phone_label_text),
        isError = uiState.contactPhoneInput.errorType != null,
        errorMessage = uiState.contactPhoneInput.errorType?.messageResourceId?.let {
            stringResource(it)
        } ?: "",
        onFocusLost = { viewModel.handle(RegisterEvent.ContactPhoneLostFocus) }
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(
            onNavigateBack = {})
    }
}
