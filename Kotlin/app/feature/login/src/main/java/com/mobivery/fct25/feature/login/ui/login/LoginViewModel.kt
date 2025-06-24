package com.mobivery.fct25.feature.login.ui.login

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.domain.repository.SessionRepository
import com.mobivery.fct25.feature.login.model.LoginFormError
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginEvent
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface LoginViewModelInterface : BaseViewModelInterface {
    val loginUiState: StateFlow<LoginUiState>
    fun handle(event: LoginEvent)
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) : BaseViewModel(), LoginViewModelInterface {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    override val loginUiState = _loginUiState.asStateFlow()

    override fun handle(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetUser -> _loginUiState.update {
                it.copy(userInput = TextFieldUiModel(text = event.value))
            }

            is LoginEvent.SetPassword -> _loginUiState.update {
                it.copy(passwordInput = TextFieldUiModel(text = event.value))
            }

            LoginEvent.UserLostFocus ->
                isValidUsername()

            LoginEvent.PasswordLostFocus ->
                isValidPassword()

            LoginEvent.OnLoginButtonClick ->
                onLoginButtonClick()

            LoginEvent.OnForgotPasswordClick ->
                _loginUiState.update { it.copy(navigation = LoginNavigation.NavigateToForgotPassword) }

            LoginEvent.DidNavigate ->
                didNavigate()
        }
    }

    private fun onLoginButtonClick() {
        val user = _loginUiState.value.userInput.text
        val password = _loginUiState.value.passwordInput.text
        if (validForm()) {
            _loginUiState.update { it.copy(isLoading = true) }
            login(user, password)
        }
    }

    private fun validForm(): Boolean {
        val validPassword = isValidPassword()
        return isValidUsername() && validPassword
    }

    private fun isValidPassword(): Boolean {
        val password = _loginUiState.value.passwordInput.text
        val isEmpty = password.isEmpty()
        val errorType = if (isEmpty) LoginFormError.EMPTY else null
        _loginUiState.update { currentState ->
            currentState.copy(
                passwordInput = currentState.passwordInput.copy(
                    errorType = errorType
                )
            )
        }
        return !isEmpty
    }

    private fun isValidUsername(): Boolean {
        val username = _loginUiState.value.userInput.text
        val isEmpty = username.isEmpty()
        val errorType = if (isEmpty) LoginFormError.EMPTY else null
        _loginUiState.update { currentState ->
            currentState.copy(
                userInput = currentState.userInput.copy(
                    errorType = errorType
                )
            )
        }
        return !isEmpty
    }

    fun login(user: String, password: String) {
        launchWithErrorWrapper(
            onError = {
                _loginUiState.update { it.copy(isLoading = false) }
                error.value = AppError.Login(R.string.login_error_credentials_text)
            }
        ) {
            val credentials = authRepository.login(user, password)
            val accessToken = credentials.accessToken
            val refreshToken = credentials.refreshToken

            if (!accessToken.isNullOrBlank()) {
                sessionRepository.saveTokens(accessToken, refreshToken)
                val user = authRepository.getUserLocal().firstOrNull()

                val navigation = when {
                    user?.role == 1 -> LoginNavigation.NavigateToStudentOffers
                    user?.role == 2 && user.companyId == null -> LoginNavigation.NavigateToRegisterCompany
                    user?.role == 2 && user.companyId != null -> LoginNavigation.NavigateToCompany
                    else -> null
                }

                if (navigation == null) {
                    _loginUiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    error.value = AppError.Login(R.string.common_default_server_error_text)
                } else {
                    _loginUiState.update {
                        it.copy(
                            navigation = navigation,
                            isLoading = false
                        )
                    }
                }
            } else {
                _loginUiState.update { it.copy(isLoading = false) }
                error.value = AppError.Login(R.string.login_error_credentials_text)
            }
        }
    }

    private fun didNavigate() {
        _loginUiState.update {
            it.copy(
                navigation = null
            )
        }
    }

    data class LoginUiState(
        val userInput: TextFieldUiModel<LoginFormError> = TextFieldUiModel(""),
        val passwordInput: TextFieldUiModel<LoginFormError> = TextFieldUiModel(""),
        val navigation: LoginNavigation? = null,
        val isLoading: Boolean = false,
    )

    sealed interface LoginNavigation {
        data object NavigateToForgotPassword : LoginNavigation
        data object NavigateToRegisterCompany : LoginNavigation
        data object NavigateToCompany : LoginNavigation
        data object NavigateToHome : LoginNavigation
        data object NavigateToStudentOffers : LoginNavigation
    }

    sealed interface LoginEvent {
        data class SetUser(val value: String) : LoginEvent
        data class SetPassword(val value: String) : LoginEvent
        data object UserLostFocus : LoginEvent
        data object PasswordLostFocus : LoginEvent
        data object OnLoginButtonClick : LoginEvent
        data object OnForgotPasswordClick : LoginEvent
        data object DidNavigate : LoginEvent
    }
}
