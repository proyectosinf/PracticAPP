package com.mobivery.fct25.feature.login.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.PasswordTextField
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXL
import com.mobivery.fct25.feature.company.ui.createcompany.navigation.CREATE_COMPANY_ROUTE
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.VIEW_COMPANY_ROUTE
import com.mobivery.fct25.feature.forgotpassword.navigation.FORGOT_PASSWORD_ROUTE
import com.mobivery.fct25.feature.home.navigation.HOME_FEATURE_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.login.R
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginEvent
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginNavigation
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginUiState
import com.mobivery.fct25.feature.login.ui.login.navigation.LOGIN_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.STUDENT_PUBLISHED_OFFERS_ROUTE
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    onExitApp: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModelInterface = hiltViewModel<LoginViewModel>(),
) {
    val uiState by viewModel.loginUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is LoginNavigation.NavigateToForgotPassword -> {
                navController.navigate(FORGOT_PASSWORD_ROUTE)
            }

            is LoginNavigation.NavigateToHome -> {
                navController.navigate(HOME_FEATURE_NAVIGATION_ROUTE) {
                    popUpTo(LOGIN_NAVIGATION_ROUTE) { inclusive = true }
                }
            }

            LoginNavigation.NavigateToCompany ->
                navController.navigate(VIEW_COMPANY_ROUTE) {
                    popUpTo(0)
                    launchSingleTop = true
                }

            LoginNavigation.NavigateToRegisterCompany ->
                navController.navigate(CREATE_COMPANY_ROUTE) {
                    popUpTo(0)
                    launchSingleTop = true
                }

            LoginNavigation.NavigateToStudentOffers -> {
                navController.navigate(STUDENT_PUBLISHED_OFFERS_ROUTE) {
                    popUpTo(LOGIN_NAVIGATION_ROUTE) { inclusive = true }
                }
            }

            null -> Unit
        }
        if (uiState.navigation != null) {
            viewModel.handle(LoginEvent.DidNavigate)
        }
    }

    BackHandler { onExitApp() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(AppColors.background)
                .imePadding()
        ) {
            Loader(viewModel = viewModel)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                LoginForm(
                    uiState,
                    onUserSet = { viewModel.handle(LoginEvent.SetUser(it)) },
                    onPasswordSet = { viewModel.handle(LoginEvent.SetPassword(it)) },
                    onUserLostFocus = { viewModel.handle(LoginEvent.UserLostFocus) },
                    onPasswordLostFocus = { viewModel.handle(LoginEvent.PasswordLostFocus) },
                    onLoginButtonClick = { viewModel.handle(LoginEvent.OnLoginButtonClick) },
                    onRegisterButtonClick = onNavigateToRegister,
                    onForgotPasswordClick = {
                        viewModel.handle(LoginEvent.OnForgotPasswordClick)
                    }
                )
            }
        }
    }
}

@Composable
private fun LoginForm(
    uiState: LoginUiState,
    onUserSet: (value: String) -> Unit,
    onPasswordSet: (value: String) -> Unit,
    onUserLostFocus: () -> Unit,
    onPasswordLostFocus: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(SPACING_S),
        ) {
            val focusManager = LocalFocusManager.current
            Image(
                painter = painterResource(id = R.drawable.practicapp),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(275.dp)
                    .padding(top = SPACING_XXL, bottom = SPACING_S)
            )
            Spacer(modifier = Modifier.height(SPACING_S))
            Text(
                text = stringResource(id = R.string.login_welcome_title_text),
                style = AppTypography.headlineSmall,
                color = AppColors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SPACING_S),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.login_welcome_body_text),
                style = AppTypography.titleLarge,
                color = AppColors.onSurfaceSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SPACING_M),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(SPACING_S))
            CustomTextField(
                value = uiState.userInput.text,
                label = stringResource(id = R.string.login_email_text),
                onValueChange = { onUserSet(it) },
                isError = uiState.userInput.errorType != null,
                errorMessage = uiState.userInput.errorType?.messageResourceId?.let {
                    stringResource(it)
                } ?: "",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                onFocusLost = { onUserLostFocus() }
            )
            Spacer(modifier = Modifier.height(SPACING_M))
            PasswordTextField(
                value = uiState.passwordInput.text,
                label = stringResource(id = R.string.login_password_text),
                onValueChange = { onPasswordSet(it) },
                isError = uiState.passwordInput.errorType != null,
                errorMessage = uiState.passwordInput.errorType?.messageResourceId?.let {
                    stringResource(it)
                } ?: "",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onLoginButtonClick() }
                ),
                onFocusLost = { onPasswordLostFocus() }
            )
            Spacer(modifier = Modifier.height(SPACING_M))
            TextButton(
                onClick = { onForgotPasswordClick() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.login_forgot_password_button),
                    style = AppTypography.bodyMedium,
                    color = AppColors.primary
                )
            }
            Spacer(modifier = Modifier.height(SPACING_XXL))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login_send_button),
                onClick = { onLoginButtonClick() },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(SPACING_M))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.login_sign_up_text),
                    style = AppTypography.bodyLarge,
                    color = AppColors.onSurface
                )
                Spacer(modifier = Modifier.width(SPACING_S))
                TextButton(onClick = onRegisterButtonClick) {
                    Text(
                        text = stringResource(id = R.string.login_sign_up_button),
                        style = AppTypography.bodyLarge,
                        color = AppColors.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    val navController = rememberNavController()

    AppTheme {
        LoginScreen(
            navController = navController,
            onExitApp = {},
            onNavigateToRegister = {},
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : LoginViewModelInterface {
        override val loginUiState = MutableStateFlow(LoginUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        override fun handle(event: LoginEvent) {}
        override fun closeError() {}
    }
}
