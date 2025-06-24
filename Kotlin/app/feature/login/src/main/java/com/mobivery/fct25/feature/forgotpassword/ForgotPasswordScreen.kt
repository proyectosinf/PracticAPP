package com.mobivery.fct25.feature.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithLeftCancel
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModelInterface = hiltViewModel<ForgotPasswordViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val emailInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState.navigateToLogin) {
        if (uiState.navigateToLogin) {
            viewModel.handle(ForgotPasswordEvent.OnNavigatedToLogin)
            onNavigateToLogin()
        }
    }

    LaunchedEffect(emailInteractionSource) {
        emailInteractionSource.interactions.collectLatest { interaction ->
            if (interaction is FocusInteraction.Unfocus) {
                viewModel.handle(ForgotPasswordEvent.EmailFocusLost)
            }
        }
    }

    if (uiState.showSuccessDialog) {
        InfoDialog(
            title = stringResource(R.string.common_forgot_password_success_title_text),
            message = stringResource(
                R.string.common_forgot_password_success_message_text,
                uiState.email.text
            ),
            primaryButton = stringResource(R.string.common_accept_text),
            onPrimaryButtonClick = {
                viewModel.handle(ForgotPasswordEvent.OnDialogDismissed)
            }
        )
    }

    Scaffold(
        topBar = {
            TopBarWithLeftCancel(
                onCancelClick = { onNavigateToLogin() },
                title = stringResource(R.string.login_password_nav_title_text),
                titleTextStyle = AppTypography.bodyLarge
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = SPACING_L)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.practicapp),
                contentDescription = null,
                modifier = Modifier
                    .size(170.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text = stringResource(R.string.login_forgot_password_title_text),
                style = AppTypography.headlineSmall,
                color = AppColors.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_password_subtitle_text),
                style = AppTypography.bodyMedium,
                color = AppColors.onSurfaceSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                value = uiState.email.text,
                label = stringResource(R.string.login_email_text),
                isError = uiState.emailHasBeenFocused && uiState.emailError != null,
                errorMessage = if (uiState.emailHasBeenFocused) uiState.emailError.orEmpty() else "",
                onValueChange = { viewModel.handle(ForgotPasswordEvent.EmailChanged(it)) },
                interactionSource = emailInteractionSource,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.login_password_send_text),
                onClick = { viewModel.handle(ForgotPasswordEvent.OnSubmitClick) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
