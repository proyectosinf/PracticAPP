package com.mobivery.fct25.feature.forgotpassword

import android.content.Context
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ForgotPasswordViewModelInterface : BaseViewModelInterface {
    val uiState: StateFlow<ForgotPasswordUiState>
    fun handle(event: ForgotPasswordEvent)
}

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel(), ForgotPasswordViewModelInterface {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    override val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    override fun handle(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = TextFieldUiModel(text = event.value),
                        emailError = if (it.emailHasBeenFocused) validateEmail(event.value) else null
                    )
                }
            }

            ForgotPasswordEvent.EmailFocusLost -> {
                val errorMessage = validateEmail(_uiState.value.email.text)
                _uiState.update {
                    it.copy(
                        emailError = errorMessage,
                        emailHasBeenFocused = true
                    )
                }
            }

            ForgotPasswordEvent.OnSubmitClick -> {
                _uiState.update { it.copy(emailHasBeenFocused = true) }
                sendResetEmail()
            }

            ForgotPasswordEvent.OnDialogDismissed -> {
                _uiState.update { it.copy(showSuccessDialog = false, navigateToLogin = true) }
            }

            ForgotPasswordEvent.OnNavigatedToLogin -> {
                _uiState.update { it.copy(navigateToLogin = false) }
            }
        }
    }

    private fun sendResetEmail() {
        val email = _uiState.value.email.text
        val emailError = validateEmail(email)

        if (emailError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    emailHasBeenFocused = true
                )
            }
            return
        }

        launchWithErrorWrapper {
            authRepository.sendPasswordResetEmail(email)
            _uiState.update { it.copy(showSuccessDialog = true) }
        }
    }

    private fun validateEmail(value: String): String? {
        return when {
            value.isBlank() -> context.getString(R.string.common_mandatory_text)
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() ->
                context.getString(R.string.register_invalid_email_text)

            else -> null
        }
    }
}

data class ForgotPasswordUiState(
    val email: TextFieldUiModel<String> = TextFieldUiModel(""),
    val emailError: String? = null,
    val emailHasBeenFocused: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val navigateToLogin: Boolean = false
)

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    object EmailFocusLost : ForgotPasswordEvent
    object OnSubmitClick : ForgotPasswordEvent
    object OnDialogDismissed : ForgotPasswordEvent
    object OnNavigatedToLogin : ForgotPasswordEvent
}
