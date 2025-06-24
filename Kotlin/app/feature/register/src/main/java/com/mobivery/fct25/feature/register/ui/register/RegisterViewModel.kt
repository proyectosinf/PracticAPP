package com.mobivery.fct25.feature.register.ui.register

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.feature.register.model.RegisterFormError
import com.mobivery.fct25.feature.register.model.RegisterGlobalError
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterEvent
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterUiState
import com.mobivery.template.domain.model.user.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

interface RegisterViewModelInterface : BaseViewModelInterface {
    val registerUiState: StateFlow<RegisterUiState>
    fun handle(event: RegisterEvent)
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @Named("firebase_auth") private val firebaseAuth: FirebaseAuth
) : BaseViewModel(), RegisterViewModelInterface {

    private companion object {
        const val MINIMUM_PASSWORD_LENGTH = 8
        const val MAX_LENGTH = 100
        val DNI_REGEX = "^[0-9]{8}[A-Z]$".toRegex()
        val SOCIAL_SECURITY_REGEX = "^[0-9]{12}$".toRegex()
        val PHONE_REGEX = "^[0-9]{9}$".toRegex()
        private const val ERROR_DNI_ALREADY_EXISTS = 7003
        private const val ERROR_SOCIAL_SECURITY_ALREADY_EXISTS = 7002
    }

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    override val registerUiState = _registerUiState.asStateFlow()

    override fun handle(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnEmailChanged ->
                _registerUiState.update {
                    it.copy(emailInput = it.emailInput.copy(text = event.email, errorType = null))
                }

            is RegisterEvent.OnPasswordChanged ->
                _registerUiState.update {
                    it.copy(
                        passwordInput = it.passwordInput.copy(
                            text = event.password,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnConfirmPasswordChanged ->
                _registerUiState.update {
                    it.copy(
                        confirmPasswordInput = it.confirmPasswordInput.copy(
                            text = event.confirmPassword,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnNameChanged ->
                _registerUiState.update {
                    it.copy(nameInput = it.nameInput.copy(text = event.name, errorType = null))
                }

            is RegisterEvent.OnSurnameChanged ->
                _registerUiState.update {
                    it.copy(
                        surnameInput = it.surnameInput.copy(
                            text = event.surname,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnRoleChanged -> updateRole(event.role)

            is RegisterEvent.OnDniChanged ->
                _registerUiState.update {
                    it.copy(dniInput = it.dniInput.copy(text = event.dni, errorType = null))
                }

            is RegisterEvent.OnSocialSecurityChanged ->
                _registerUiState.update {
                    it.copy(
                        socialSecurityInput = it.socialSecurityInput.copy(
                            text = event.socialSecurity,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnContactNameChanged ->
                _registerUiState.update {
                    it.copy(
                        contactNameInput = it.contactNameInput.copy(
                            text = event.contactName,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnContactEmailChanged ->
                _registerUiState.update {
                    it.copy(
                        contactEmailInput = it.contactEmailInput.copy(
                            text = event.contactEmail,
                            errorType = null
                        )
                    )
                }

            is RegisterEvent.OnContactPhoneChanged ->
                _registerUiState.update {
                    it.copy(
                        contactPhoneInput = it.contactPhoneInput.copy(
                            text = event.contactPhone,
                            errorType = null
                        )
                    )
                }

            RegisterEvent.EmailLostFocus -> validateEmail()
            RegisterEvent.PasswordLostFocus -> validatePassword()
            RegisterEvent.ConfirmPasswordLostFocus -> validateConfirmPassword()
            RegisterEvent.NameLostFocus -> validateName()
            RegisterEvent.SurnameLostFocus -> validateSurname()
            RegisterEvent.RoleLostFocus -> validateRole()
            RegisterEvent.RegisterClicked -> onRegisterClick()
            RegisterEvent.DniLostFocus -> validateOptionalDni()
            RegisterEvent.SocialSecurityLostFocus -> validateOptionalSocialSecurity()
            RegisterEvent.ContactNameLostFocus -> validateOptionalContactName()
            RegisterEvent.ContactEmailLostFocus -> validateOptionalContactEmail()
            RegisterEvent.ContactPhoneLostFocus -> validateOptionalContactPhone()
            RegisterEvent.ErrorDismissed -> {
                _registerUiState.update { it.copy(globalError = null) }
            }
        }
    }

    private fun updateRole(role: UserRole) {
        _registerUiState.update {
            it.copy(
                roleInput = it.roleInput.copy(text = role.name, errorType = null),
                selectedRole = role
            )
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return emailRegex.matches(email)
    }

    private fun validateEmail(): Boolean {
        val currentEmail = _registerUiState.value.emailInput
        val errorType = when {
            currentEmail.text.isEmpty() -> RegisterFormError.EMPTY
            !isEmailValid(currentEmail.text) -> RegisterFormError.INVALID_EMAIL
            else -> null
        }
        _registerUiState.update {
            it.copy(emailInput = it.emailInput.copy(errorType = errorType))
        }
        return errorType == null
    }

    private fun validatePassword(): Boolean {
        val currentPassword = _registerUiState.value.passwordInput
        val errorType = when {
            currentPassword.text.isEmpty() -> RegisterFormError.EMPTY
            !isValidPassword(currentPassword.text) -> RegisterFormError.INVALID_PASSWORD
            else -> null
        }
        _registerUiState.update {
            it.copy(passwordInput = it.passwordInput.copy(errorType = errorType))
        }
        return errorType == null
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= MINIMUM_PASSWORD_LENGTH &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { !it.isLetterOrDigit() }
    }

    private fun validateConfirmPassword(): Boolean {
        val currentConfirmPassword = _registerUiState.value.confirmPasswordInput
        val password = _registerUiState.value.passwordInput.text
        val errorType = when {
            currentConfirmPassword.text.isEmpty() -> RegisterFormError.EMPTY
            password != currentConfirmPassword.text -> RegisterFormError.PASSWORDS_NOT_MATCH
            else -> null
        }
        _registerUiState.update {
            it.copy(confirmPasswordInput = it.confirmPasswordInput.copy(errorType = errorType))
        }
        return errorType == null
    }

    private fun validateName(): Boolean {
        val currentName = _registerUiState.value.nameInput
        val errorType = when {
            currentName.text.isEmpty() -> RegisterFormError.EMPTY
            currentName.text.length > MAX_LENGTH -> RegisterFormError.MAX_LENGTH_EXCEEDED
            else -> null
        }
        _registerUiState.update {
            it.copy(nameInput = it.nameInput.copy(errorType = errorType))
        }
        return errorType == null
    }

    private fun validateSurname(): Boolean {
        val currentSurname = _registerUiState.value.surnameInput
        val errorType = when {
            currentSurname.text.isEmpty() -> RegisterFormError.EMPTY
            currentSurname.text.length > MAX_LENGTH -> RegisterFormError.MAX_LENGTH_EXCEEDED
            else -> null
        }
        _registerUiState.update {
            it.copy(surnameInput = it.surnameInput.copy(errorType = errorType))
        }
        return errorType == null
    }

    private fun validateRole(): Boolean {
        _registerUiState.update {
            it.copy(roleInput = it.roleInput.copy(errorType = null))
        }
        return true
    }

    private fun validateStudentFields(): Boolean {
        return (validateOptionalDni()
                && validateOptionalSocialSecurity()
                && validateOptionalContactName()
                && validateOptionalContactEmail()
                && validateOptionalContactPhone()
                ).also { isValid ->
                if (isValid) clearAllStudentFieldErrors()
            }
    }

    private fun validateOptionalDni(): Boolean {
        val dni = _registerUiState.value.dniInput.text
        return when {
            dni.isBlank() -> true
            !isValidDni(dni) -> {
                _registerUiState.update {
                    it.copy(dniInput = it.dniInput.copy(errorType = RegisterFormError.INVALID_DNI))
                }
                false
            }

            else -> true
        }
    }

    private fun validateOptionalSocialSecurity(): Boolean {
        val socialSecurity = _registerUiState.value.socialSecurityInput.text
        return when {
            socialSecurity.isBlank() -> true
            !isValidSocialSecurity(socialSecurity) -> {
                _registerUiState.update {
                    it.copy(socialSecurityInput = it.socialSecurityInput.copy(errorType = RegisterFormError.INVALID_SOCIAL_SECURITY))
                }
                false
            }

            else -> true
        }
    }

    private fun validateOptionalContactName(): Boolean {
        val contactName = _registerUiState.value.contactNameInput.text
        return when {
            contactName.isBlank() -> true
            contactName.length > MAX_LENGTH -> {
                _registerUiState.update {
                    it.copy(contactNameInput = it.contactNameInput.copy(errorType = RegisterFormError.MAX_LENGTH_EXCEEDED))
                }
                false
            }

            else -> true
        }
    }

    private fun validateOptionalContactEmail(): Boolean {
        val contactEmail = _registerUiState.value.contactEmailInput.text
        return when {
            contactEmail.isBlank() -> true
            !isEmailValid(contactEmail) -> {
                _registerUiState.update {
                    it.copy(contactEmailInput = it.contactEmailInput.copy(errorType = RegisterFormError.INVALID_EMAIL))
                }
                false
            }

            else -> true
        }
    }

    private fun validateOptionalContactPhone(): Boolean {
        val contactPhone = _registerUiState.value.contactPhoneInput.text
        return when {
            contactPhone.isBlank() -> true
            !PHONE_REGEX.matches(contactPhone) -> {
                _registerUiState.update {
                    it.copy(contactPhoneInput = it.contactPhoneInput.copy(errorType = RegisterFormError.INVALID_PHONE))
                }
                false
            }

            else -> true
        }
    }

    private fun clearAllStudentFieldErrors(): Boolean {
        _registerUiState.update {
            it.copy(
                dniInput = it.dniInput.copy(errorType = null),
                socialSecurityInput = it.socialSecurityInput.copy(errorType = null),
                contactNameInput = it.contactNameInput.copy(errorType = null),
                contactEmailInput = it.contactEmailInput.copy(errorType = null),
                contactPhoneInput = it.contactPhoneInput.copy(errorType = null)
            )
        }
        return true
    }

    private fun isValidDni(dni: String): Boolean =
        DNI_REGEX.matches(dni)

    private fun isValidSocialSecurity(socialSecurity: String): Boolean =
        SOCIAL_SECURITY_REGEX.matches(socialSecurity)

    private fun validateForm(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        val confirmPasswordValid = validateConfirmPassword()
        val nameValid = validateName()
        val surnameValid = validateSurname()
        val roleValid = validateRole()

        val roleSpecificFieldsValid = when (_registerUiState.value.selectedRole) {
            UserRole.STUDENT -> validateStudentFields()
            UserRole.TUTOR -> true
        }

        return emailValid && passwordValid && confirmPasswordValid &&
                nameValid && surnameValid && roleValid && roleSpecificFieldsValid
    }

    private fun hasNoErrors(state: RegisterUiState): Boolean {
        return state.emailInput.errorType == null &&
                state.passwordInput.errorType == null &&
                state.confirmPasswordInput.errorType == null &&
                state.nameInput.errorType == null &&
                state.surnameInput.errorType == null &&
                state.roleInput.errorType == null &&
                when (state.selectedRole) {
                    UserRole.STUDENT -> {
                        (state.dniInput.text.isEmpty() || state.dniInput.errorType == null) &&
                                (state.socialSecurityInput.text.isEmpty() || state.socialSecurityInput.errorType == null) &&
                                (state.contactNameInput.text.isEmpty() || state.contactNameInput.errorType == null) &&
                                (state.contactEmailInput.text.isEmpty() || state.contactEmailInput.errorType == null) &&
                                (state.contactPhoneInput.text.isEmpty() || state.contactPhoneInput.errorType == null)
                    }

                    UserRole.TUTOR -> true
                }
    }

    private fun onRegisterClick() {
        if (_registerUiState.value.isLoading) return

        _registerUiState.update { it.copy(isLoading = true) }

        launchWithErrorWrapper(
            onError = {
                _registerUiState.update { it.copy(isLoading = false) }
            }
        ) {
            validateForm()
            if (hasNoErrors(_registerUiState.value)) {
                doRegister()
            } else {
                _registerUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun doRegister() = errorWrapper {
        val state = _registerUiState.value
        val email = state.emailInput.text
        val password = state.passwordInput.text

        try {
            val firebaseResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val firebaseUid = firebaseResult.user?.uid
            if (firebaseUid == null) {
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        globalError = RegisterGlobalError.FIREBASE_USER_NULL
                    )
                }
                return@errorWrapper
            }

            val token = firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
            if (token == null) {
                _registerUiState.update {
                    it.copy(
                        isLoading = false,
                        globalError = RegisterGlobalError.FIREBASE_TOKEN_ERROR
                    )
                }
                return@errorWrapper
            }

            val role = state.selectedRole

            val user = com.mobivery.template.domain.model.user.User(
                id = 0,
                firebaseUid = firebaseUid,
                name = state.nameInput.text,
                surname = state.surnameInput.text,
                email = email,
                dni = state.dniInput.text.takeIf { it.isNotBlank() },
                socialSecurityNumber = state.socialSecurityInput.text.takeIf { it.isNotBlank() },
                contactName = state.contactNameInput.text.takeIf { it.isNotBlank() },
                contactEmail = state.contactEmailInput.text.takeIf { it.isNotBlank() },
                contactPhone = state.contactPhoneInput.text.takeIf { it.isNotBlank() },
                role = role,
                companyId = null,
                photo = "",
                pdfCV = null
            )

            authRepository.saveFirebaseToken(token)

            try {
                authRepository.register(
                    user = user,
                    password = password,
                    firebaseToken = token
                )
            } catch (error: Throwable) {
                _registerUiState.update { it.copy(isLoading = false) }

                if (error is AppError.ApiError) {
                    val globalError = when (error.code) {
                        ERROR_DNI_ALREADY_EXISTS -> RegisterGlobalError.BACKEND_DNI_ALREADY_EXISTS
                        ERROR_SOCIAL_SECURITY_ALREADY_EXISTS -> RegisterGlobalError.BACKEND_SOCIAL_SECURITY_NUMBER_ALREADY_EXISTS
                        else -> RegisterGlobalError.BACKEND_ERROR
                    }
                    _registerUiState.update { it.copy(globalError = globalError) }
                } else {
                    _registerUiState.update {
                        it.copy(globalError = RegisterGlobalError.BACKEND_ERROR)
                    }
                }

                firebaseAuth.currentUser?.let { userToDelete ->
                    try {
                        userToDelete.delete().await()
                    } catch (error: Exception) {
                        Log.e(this::class.simpleName, "doRegister - ${error.localizedMessage}", error)
                    }
                }

                return@errorWrapper
            }

            _registerUiState.update { it.copy(isLoading = false) }
        } catch (error: Throwable) {
            _registerUiState.update {
                it.copy(
                    isLoading = false,
                    globalError = when {
                        error is FirebaseAuthUserCollisionException -> RegisterGlobalError.FIREBASE_ERROR
                        else -> RegisterGlobalError.FIREBASE_ERROR
                    }
                )
            }
        }
    }

    data class RegisterUiState(
        val emailInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val passwordInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val confirmPasswordInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val nameInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel("", errorType = null),
        val surnameInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val roleInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel("", errorType = null),
        val selectedRole: UserRole = UserRole.STUDENT,
        val dniInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel("", errorType = null),
        val socialSecurityInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val contactNameInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val contactEmailInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val contactPhoneInput: TextFieldUiModel<RegisterFormError> = TextFieldUiModel(
            "",
            errorType = null
        ),
        val isLoading: Boolean = false,
        val globalError: RegisterGlobalError? = null
    )

    sealed interface RegisterEvent {
        data class OnEmailChanged(val email: String) : RegisterEvent
        data class OnPasswordChanged(val password: String) : RegisterEvent
        data class OnConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent
        data class OnNameChanged(val name: String) : RegisterEvent
        data class OnSurnameChanged(val surname: String) : RegisterEvent
        data class OnRoleChanged(val role: UserRole) : RegisterEvent
        data class OnDniChanged(val dni: String) : RegisterEvent
        data class OnSocialSecurityChanged(val socialSecurity: String) : RegisterEvent
        data class OnContactNameChanged(val contactName: String) : RegisterEvent
        data class OnContactEmailChanged(val contactEmail: String) : RegisterEvent
        data class OnContactPhoneChanged(val contactPhone: String) : RegisterEvent
        data object EmailLostFocus : RegisterEvent
        data object PasswordLostFocus : RegisterEvent
        data object ConfirmPasswordLostFocus : RegisterEvent
        data object NameLostFocus : RegisterEvent
        data object SurnameLostFocus : RegisterEvent
        data object RoleLostFocus : RegisterEvent
        data object RegisterClicked : RegisterEvent
        data object ErrorDismissed : RegisterEvent
        data object DniLostFocus : RegisterEvent
        data object SocialSecurityLostFocus : RegisterEvent
        data object ContactNameLostFocus : RegisterEvent
        data object ContactEmailLostFocus : RegisterEvent
        data object ContactPhoneLostFocus : RegisterEvent
    }
}