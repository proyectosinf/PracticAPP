package com.mobivery.fct25.feature.register.ui.register

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.feature.register.model.RegisterFormError
import com.mobivery.fct25.feature.register.model.RegisterGlobalError
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterEvent
import com.mobivery.fct25.testing.utils.MainDispatcherRule
import com.mobivery.template.domain.model.auth.RegisterResult
import com.mobivery.template.domain.model.user.UserRole
import io.mockk.MockKAnnotations
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherRule::class)
class RegisterViewModelTests {

    private companion object {
        const val REGISTER_SUCCESS_MESSAGE = "User registered successfully"
        const val FAKE_ACCESS_TOKEN = "fakeAccessToken"
        const val FAKE_REFRESH_TOKEN = "fakeRefreshToken"
        const val VALID_EMAIL = "user@example.com"
        const val INVALID_EMAIL = "invalid_email"
        const val VALID_PASSWORD = "Password123!"
        const val SHORT_PASSWORD = "short"
        const val MISMATCH_PASSWORD = "Different123!"
        const val VALID_NAME = "John"
        val LONG_NAME = "a".repeat(101)
        const val VALID_SURNAME = "Doe"
        val LONG_SURNAME = "b".repeat(101)
        const val VALID_DNI = "12345678Z"
        const val INVALID_DNI = "12ABCZ78"
        const val VALID_SS = "123456789012"
        const val INVALID_SS = "123"
        const val VALID_CONTACT_NAME = "Contact Person"
        val LONG_CONTACT_NAME = "x".repeat(101)
        const val VALID_CONTACT_EMAIL = "contact@example.com"
        const val INVALID_CONTACT_EMAIL = "contact@@bad"
        const val VALID_CONTACT_PHONE = "600123456"
        const val INVALID_CONTACT_PHONE = "abc123"
        const val TEST_UID = "test-uid"
        const val TEST_ACCESS_TOKEN = "test-access-token"
        const val TEST_REFRESH_TOKEN = "test-refresh-token"
        const val LOWERCASE_ONLY_PASSWORD = "onlylowercase"
        const val UPPERCASE_ONLY_PASSWORD = "ONLYUPPERCASE"
        const val NUMBERS_ONLY_PASSWORD = "12345678"
        const val NO_SPECIAL_CHAR_PASSWORD = "NoSpecial1"
        const val INVALID_PASSWORD_MESSAGE = "Password '%s' should be invalid"
    }

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: RegisterViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        coEvery { authRepository.register(any(), any(), any()) } returns RegisterResult(
            success = true,
            message = REGISTER_SUCCESS_MESSAGE,
            accessToken = FAKE_ACCESS_TOKEN,
            refreshToken = FAKE_REFRESH_TOKEN
        )

        every {
            firebaseAuth.createUserWithEmailAndPassword(any(), any())
        } returns mockk(relaxed = true)

        viewModel = RegisterViewModel(authRepository, firebaseAuth)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(authRepository, firebaseAuth)
    }

    // Email validation tests
    @Test
    fun `GIVEN empty email WHEN EmailLostFocus THEN error is EMPTY`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnEmailChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.EmailLostFocus)

        // THEN
        assertEquals(RegisterFormError.EMPTY, viewModel.registerUiState.value.emailInput.errorType)
    }

    @Test
    fun `GIVEN invalid email WHEN EmailLostFocus THEN error is INVALID_EMAIL`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnEmailChanged(INVALID_EMAIL))

        // WHEN
        viewModel.handle(RegisterEvent.EmailLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.INVALID_EMAIL,
            viewModel.registerUiState.value.emailInput.errorType
        )
    }

    // Password validation tests
    @Test
    fun `GIVEN empty password WHEN PasswordLostFocus THEN error is EMPTY`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnPasswordChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.PasswordLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.EMPTY,
            viewModel.registerUiState.value.passwordInput.errorType
        )
    }

    @Test
    fun `GIVEN invalid password WHEN PasswordLostFocus THEN error is INVALID_PASSWORD`() = runTest {
        // GIVEN
        val invalidPasswords = listOf(
            SHORT_PASSWORD,
            LOWERCASE_ONLY_PASSWORD,
            UPPERCASE_ONLY_PASSWORD,
            NUMBERS_ONLY_PASSWORD,
            NO_SPECIAL_CHAR_PASSWORD
        )

        invalidPasswords.forEach { password ->
            viewModel.handle(RegisterEvent.OnPasswordChanged(password))

            // WHEN
            viewModel.handle(RegisterEvent.PasswordLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.INVALID_PASSWORD,
                viewModel.registerUiState.value.passwordInput.errorType,
                INVALID_PASSWORD_MESSAGE.format(password)
            )
        }
    }

    // Confirm password validation tests
    @Test
    fun `GIVEN empty confirm password WHEN ConfirmPasswordLostFocus THEN error is EMPTY`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnConfirmPasswordChanged(""))

            // WHEN
            viewModel.handle(RegisterEvent.ConfirmPasswordLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.EMPTY,
                viewModel.registerUiState.value.confirmPasswordInput.errorType
            )
        }

    @Test
    fun `GIVEN mismatched passwords WHEN ConfirmPasswordLostFocus THEN error is PASSWORDS_NOT_MATCH`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
            viewModel.handle(RegisterEvent.OnConfirmPasswordChanged(MISMATCH_PASSWORD))

            // WHEN
            viewModel.handle(RegisterEvent.ConfirmPasswordLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.PASSWORDS_NOT_MATCH,
                viewModel.registerUiState.value.confirmPasswordInput.errorType
            )
        }

    // Name validation tests
    @Test
    fun `GIVEN empty name WHEN NameLostFocus THEN error is NAME_REQUIRED`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnNameChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.NameLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.EMPTY,
            viewModel.registerUiState.value.nameInput.errorType
        )
    }

    @Test
    fun `GIVEN long name WHEN NameLostFocus THEN error is MAX_LENGTH_EXCEEDED`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnNameChanged(LONG_NAME))

        // WHEN
        viewModel.handle(RegisterEvent.NameLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.MAX_LENGTH_EXCEEDED,
            viewModel.registerUiState.value.nameInput.errorType
        )
    }

    // Surname validation tests
    @Test
    fun `GIVEN empty surname WHEN SurnameLostFocus THEN error is LASTNAME_REQUIRED`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnSurnameChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.SurnameLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.EMPTY,
            viewModel.registerUiState.value.surnameInput.errorType
        )
    }

    @Test
    fun `GIVEN long surname WHEN SurnameLostFocus THEN error is MAX_LENGTH_EXCEEDED`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnSurnameChanged(LONG_SURNAME))

        // WHEN
        viewModel.handle(RegisterEvent.SurnameLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.MAX_LENGTH_EXCEEDED,
            viewModel.registerUiState.value.surnameInput.errorType
        )
    }

    // Optional field validation tests
    @Test
    fun `GIVEN invalid dni WHEN RegisterClicked THEN error is INVALID_DNI`() = runTest {
        // GIVEN
        setupRequiredFields()
        viewModel.handle(RegisterEvent.OnDniChanged(INVALID_DNI))

        // WHEN
        viewModel.handle(RegisterEvent.RegisterClicked)
        advanceUntilIdle()

        // THEN
        assertEquals(
            RegisterFormError.INVALID_DNI,
            viewModel.registerUiState.value.dniInput.errorType
        )
    }

    @Test
    fun `GIVEN invalid social security WHEN RegisterClicked THEN error is INVALID_SOCIAL_SECURITY`() =
        runTest {
            // GIVEN
            setupRequiredFields()
            viewModel.handle(RegisterEvent.OnSocialSecurityChanged(INVALID_SS))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            assertEquals(
                RegisterFormError.INVALID_SOCIAL_SECURITY,
                viewModel.registerUiState.value.socialSecurityInput.errorType
            )
        }

    @Test
    fun `GIVEN long contact name WHEN RegisterClicked THEN error is MAX_LENGTH_EXCEEDED`() =
        runTest {
            // GIVEN
            setupRequiredFields()
            viewModel.handle(RegisterEvent.OnContactNameChanged(LONG_CONTACT_NAME))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            assertEquals(
                RegisterFormError.MAX_LENGTH_EXCEEDED,
                viewModel.registerUiState.value.contactNameInput.errorType
            )
        }

    @Test
    fun `GIVEN invalid contact email WHEN RegisterClicked THEN error is INVALID_EMAIL`() = runTest {
        // GIVEN
        setupRequiredFields()
        viewModel.handle(RegisterEvent.OnContactEmailChanged(INVALID_CONTACT_EMAIL))

        // WHEN
        viewModel.handle(RegisterEvent.RegisterClicked)
        advanceUntilIdle()

        // THEN
        assertEquals(
            RegisterFormError.INVALID_EMAIL,
            viewModel.registerUiState.value.contactEmailInput.errorType
        )
    }

    @Test
    fun `GIVEN invalid contact phone WHEN RegisterClicked THEN error is INVALID_PHONE`() = runTest {
        // GIVEN
        setupRequiredFields()
        viewModel.handle(RegisterEvent.OnContactPhoneChanged(INVALID_CONTACT_PHONE))

        // WHEN
        viewModel.handle(RegisterEvent.RegisterClicked)
        advanceUntilIdle()

        // THEN
        assertEquals(
            RegisterFormError.INVALID_PHONE,
            viewModel.registerUiState.value.contactPhoneInput.errorType
        )
    }

    // Combined registration tests
    @Test
    fun `GIVEN tutor role and filled security code WHEN RegisterClicked THEN registration is successful`() =
        runTest {
            // GIVEN
            setupSuccessfulMocks()

            with(viewModel) {
                handle(RegisterEvent.OnEmailChanged(VALID_EMAIL))
                handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
                handle(RegisterEvent.OnConfirmPasswordChanged(VALID_PASSWORD))
                handle(RegisterEvent.OnNameChanged(VALID_NAME))
                handle(RegisterEvent.OnSurnameChanged(VALID_SURNAME))
                handle(RegisterEvent.OnRoleChanged(UserRole.TUTOR))
            }

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verifySuccessfulRegistrationWithRole(UserRole.TUTOR)
            confirmVerified(authRepository, firebaseAuth)
        }

    @Test
    fun `GIVEN missing required fields WHEN RegisterClicked THEN errors are set correctly`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnConfirmPasswordChanged(MISMATCH_PASSWORD))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            with(viewModel.registerUiState.value) {
                assertEquals(RegisterFormError.EMPTY, emailInput.errorType)
                assertEquals(RegisterFormError.EMPTY, passwordInput.errorType)
                assertEquals(RegisterFormError.PASSWORDS_NOT_MATCH, confirmPasswordInput.errorType)
                assertEquals(RegisterFormError.EMPTY, nameInput.errorType)
                assertEquals(RegisterFormError.EMPTY, surnameInput.errorType)
            }
        }

    @Test
    fun `GIVEN optional fields missing WHEN RegisterClicked THEN no error for optional fields`() =
        runTest {
            // GIVEN
            setupSuccessfulMocks()
            setupRequiredFields()

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verifyNoErrorsInOptionalFields()

            verify(exactly = 1) {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
                firebaseAuth.currentUser
            }

            coVerify(ordering = Ordering.SEQUENCE) {
                authRepository.saveFirebaseToken(TEST_ACCESS_TOKEN)
                authRepository.register(any(), VALID_PASSWORD, TEST_ACCESS_TOKEN)
            }
        }

    @Test
    fun `GIVEN valid optional fields WHEN RegisterClicked THEN no error for optional fields`() =
        runTest {
            // GIVEN
            setupSuccessfulMocks()
            setupRequiredFields()

            with(viewModel) {
                handle(RegisterEvent.OnDniChanged(VALID_DNI))
                handle(RegisterEvent.OnSocialSecurityChanged(VALID_SS))
                handle(RegisterEvent.OnContactNameChanged(VALID_CONTACT_NAME))
                handle(RegisterEvent.OnContactEmailChanged(VALID_CONTACT_EMAIL))
                handle(RegisterEvent.OnContactPhoneChanged(VALID_CONTACT_PHONE))
            }

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verifyNoErrorsInOptionalFields()
            verifySuccessfulRegistrationWithRole(UserRole.STUDENT)
        }

    @Test
    fun `GIVEN long name and surname WHEN RegisterClicked THEN MAX_LENGTH_EXCEEDED errors`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnEmailChanged(VALID_EMAIL))
            viewModel.handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
            viewModel.handle(RegisterEvent.OnConfirmPasswordChanged(VALID_PASSWORD))
            viewModel.handle(RegisterEvent.OnNameChanged(LONG_NAME))
            viewModel.handle(RegisterEvent.OnSurnameChanged(LONG_SURNAME))
            viewModel.handle(RegisterEvent.OnRoleChanged(UserRole.STUDENT))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            assertEquals(
                RegisterFormError.MAX_LENGTH_EXCEEDED,
                viewModel.registerUiState.value.nameInput.errorType
            )
            assertEquals(
                RegisterFormError.MAX_LENGTH_EXCEEDED,
                viewModel.registerUiState.value.surnameInput.errorType
            )
        }

    @Test
    fun `GIVEN valid user data WHEN RegisterClicked THEN registration is successful`() = runTest {
        // GIVEN
        setupSuccessfulMocks()
        setupRequiredFieldsWithValidation()

        // WHEN
        viewModel.handle(RegisterEvent.RegisterClicked)
        advanceUntilIdle()

        // THEN
        verifySuccessfulRegistrationWithRole(UserRole.STUDENT)
        assertEquals(null, viewModel.registerUiState.value.emailInput.errorType)
    }

    @Test
    fun `GIVEN firebase user is null WHEN RegisterClicked THEN global error is FIREBASE_USER_NULL`() =
        runTest {
            // GIVEN
            setupRequiredFields()

            every {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
            } returns Tasks.forResult(mockk<AuthResult> {
                every { user } returns null
            })

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verify {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
            }

            assertEquals(
                RegisterGlobalError.FIREBASE_USER_NULL,
                viewModel.registerUiState.value.globalError
            )
        }

    @Test
    fun `GIVEN null firebase token WHEN RegisterClicked THEN global error is FIREBASE_TOKEN_ERROR`() =
        runTest {
            // GIVEN
            val firebaseUser = mockk<FirebaseUser>(relaxed = true)
            val tokenResult = mockk<GetTokenResult> {
                every { token } returns null
            }

            setupRequiredFields()

            every {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
            } returns Tasks.forResult(mockk<AuthResult> {
                every { user } returns firebaseUser
            })

            every { firebaseAuth.currentUser } returns firebaseUser
            every { firebaseUser.getIdToken(false) } returns Tasks.forResult(tokenResult)

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            assertEquals(
                RegisterGlobalError.FIREBASE_TOKEN_ERROR,
                viewModel.registerUiState.value.globalError
            )

            verify {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
                firebaseAuth.currentUser
                firebaseUser.getIdToken(false)
            }
        }

    @Test
    fun `GIVEN role changed WHEN OnRoleChanged THEN selectedRole is updated`() = runTest {
        // WHEN
        viewModel.handle(RegisterEvent.OnRoleChanged(UserRole.TUTOR))

        // THEN
        assertEquals(UserRole.TUTOR, viewModel.registerUiState.value.selectedRole)
    }

    @Test
    fun `GIVEN role lost focus WHEN RoleLostFocus THEN no error is set`() = runTest {
        // WHEN
        viewModel.handle(RegisterEvent.RoleLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.roleInput.errorType)
    }

    @Test
    fun `GIVEN valid optional fields WHEN RegisterClicked THEN optional errors are cleared`() =
        runTest {
            // GIVEN
            setupSuccessfulMocks()
            setupRequiredFields()

            viewModel.handle(RegisterEvent.OnDniChanged(VALID_DNI))
            viewModel.handle(RegisterEvent.OnSocialSecurityChanged(VALID_SS))
            viewModel.handle(RegisterEvent.OnContactNameChanged(VALID_CONTACT_NAME))
            viewModel.handle(RegisterEvent.OnContactEmailChanged(VALID_CONTACT_EMAIL))
            viewModel.handle(RegisterEvent.OnContactPhoneChanged(VALID_CONTACT_PHONE))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verifyNoErrorsInOptionalFields()
            verifySuccessfulRegistrationWithRole(UserRole.STUDENT)
        }

    @Test
    fun `GIVEN valid dni WHEN DniLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnDniChanged(VALID_DNI))

        // WHEN
        viewModel.handle(RegisterEvent.DniLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.dniInput.errorType)
    }

    @Test
    fun `GIVEN invalid dni WHEN DniLostFocus THEN error is INVALID_DNI`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnDniChanged(INVALID_DNI))

        // WHEN
        viewModel.handle(RegisterEvent.DniLostFocus)

        // THEN
        assertEquals(
            RegisterFormError.INVALID_DNI,
            viewModel.registerUiState.value.dniInput.errorType
        )
    }

    @Test
    fun `GIVEN empty dni WHEN DniLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnDniChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.DniLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.dniInput.errorType)
    }

    @Test
    fun `GIVEN valid social security WHEN SocialSecurityLostFocus THEN no error is set`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnSocialSecurityChanged(VALID_SS))

            // WHEN
            viewModel.handle(RegisterEvent.SocialSecurityLostFocus)

            // THEN
            assertEquals(null, viewModel.registerUiState.value.socialSecurityInput.errorType)
        }

    @Test
    fun `GIVEN invalid social security WHEN SocialSecurityLostFocus THEN error is INVALID_SOCIAL_SECURITY`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnSocialSecurityChanged(INVALID_SS))

            // WHEN
            viewModel.handle(RegisterEvent.SocialSecurityLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.INVALID_SOCIAL_SECURITY,
                viewModel.registerUiState.value.socialSecurityInput.errorType
            )
        }

    @Test
    fun `GIVEN empty social security WHEN SocialSecurityLostFocus THEN no error is set`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnSocialSecurityChanged(""))

            // WHEN
            viewModel.handle(RegisterEvent.SocialSecurityLostFocus)

            // THEN
            assertEquals(null, viewModel.registerUiState.value.socialSecurityInput.errorType)
        }

    @Test
    fun `GIVEN valid contact name WHEN ContactNameLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactNameChanged(VALID_CONTACT_NAME))

        // WHEN
        viewModel.handle(RegisterEvent.ContactNameLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactNameInput.errorType)
    }

    @Test
    fun `GIVEN long contact name WHEN ContactNameLostFocus THEN error is MAX_LENGTH_EXCEEDED`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnContactNameChanged(LONG_CONTACT_NAME))

            // WHEN
            viewModel.handle(RegisterEvent.ContactNameLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.MAX_LENGTH_EXCEEDED,
                viewModel.registerUiState.value.contactNameInput.errorType
            )
        }

    @Test
    fun `GIVEN empty contact name WHEN ContactNameLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactNameChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.ContactNameLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactNameInput.errorType)
    }

    @Test
    fun `GIVEN valid contact email WHEN ContactEmailLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactEmailChanged(VALID_CONTACT_EMAIL))

        // WHEN
        viewModel.handle(RegisterEvent.ContactEmailLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactEmailInput.errorType)
    }

    @Test
    fun `GIVEN invalid contact email WHEN ContactEmailLostFocus THEN error is INVALID_EMAIL`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnContactEmailChanged(INVALID_CONTACT_EMAIL))

            // WHEN
            viewModel.handle(RegisterEvent.ContactEmailLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.INVALID_EMAIL,
                viewModel.registerUiState.value.contactEmailInput.errorType
            )
        }

    @Test
    fun `GIVEN empty contact email WHEN ContactEmailLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactEmailChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.ContactEmailLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactEmailInput.errorType)
    }

    @Test
    fun `GIVEN valid contact phone WHEN ContactPhoneLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactPhoneChanged(VALID_CONTACT_PHONE))

        // WHEN
        viewModel.handle(RegisterEvent.ContactPhoneLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactPhoneInput.errorType)
    }

    @Test
    fun `GIVEN invalid contact phone WHEN ContactPhoneLostFocus THEN error is INVALID_PHONE`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnContactPhoneChanged(INVALID_CONTACT_PHONE))

            // WHEN
            viewModel.handle(RegisterEvent.ContactPhoneLostFocus)

            // THEN
            assertEquals(
                RegisterFormError.INVALID_PHONE,
                viewModel.registerUiState.value.contactPhoneInput.errorType
            )
        }

    @Test
    fun `GIVEN empty contact phone WHEN ContactPhoneLostFocus THEN no error is set`() = runTest {
        // GIVEN
        viewModel.handle(RegisterEvent.OnContactPhoneChanged(""))

        // WHEN
        viewModel.handle(RegisterEvent.ContactPhoneLostFocus)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.contactPhoneInput.errorType)
    }

    @Test
    fun `GIVEN global error set WHEN ErrorDismissed THEN error is cleared`() = runTest {
        // GIVEN
        setupRequiredFields()
        every {
            firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
        } returns Tasks.forResult(mockk<AuthResult> {
            every { user } returns null
        })

        viewModel.handle(RegisterEvent.RegisterClicked)
        advanceUntilIdle()

        assertEquals(
            RegisterGlobalError.FIREBASE_USER_NULL,
            viewModel.registerUiState.value.globalError
        )

        // WHEN
        viewModel.handle(RegisterEvent.ErrorDismissed)

        // THEN
        assertEquals(null, viewModel.registerUiState.value.globalError)

        verify {
            firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
        }
    }

    @Test
    fun `GIVEN required fields with errors WHEN RegisterClicked THEN required field errors are set`() =
        runTest {
            // GIVEN
            viewModel.handle(RegisterEvent.OnEmailChanged(INVALID_EMAIL))
            viewModel.handle(RegisterEvent.OnPasswordChanged(SHORT_PASSWORD))
            viewModel.handle(RegisterEvent.OnConfirmPasswordChanged(MISMATCH_PASSWORD))
            viewModel.handle(RegisterEvent.OnNameChanged(""))
            viewModel.handle(RegisterEvent.OnSurnameChanged(""))

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            with(viewModel.registerUiState.value) {
                assertEquals(RegisterFormError.INVALID_EMAIL, emailInput.errorType)
                assertEquals(RegisterFormError.INVALID_PASSWORD, passwordInput.errorType)
                assertEquals(RegisterFormError.PASSWORDS_NOT_MATCH, confirmPasswordInput.errorType)
                assertEquals(RegisterFormError.EMPTY, nameInput.errorType)
                assertEquals(RegisterFormError.EMPTY, surnameInput.errorType)
            }
        }

    @Test
    fun `GIVEN tutor role with invalid optional fields WHEN RegisterClicked THEN registration succeeds`() =
        runTest {
            // GIVEN
            setupSuccessfulMocks()

            with(viewModel) {
                handle(RegisterEvent.OnEmailChanged(VALID_EMAIL))
                handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
                handle(RegisterEvent.OnConfirmPasswordChanged(VALID_PASSWORD))
                handle(RegisterEvent.OnNameChanged(VALID_NAME))
                handle(RegisterEvent.OnSurnameChanged(VALID_SURNAME))
                handle(RegisterEvent.OnRoleChanged(UserRole.TUTOR))
                handle(RegisterEvent.OnDniChanged(INVALID_DNI))
                handle(RegisterEvent.OnSocialSecurityChanged(INVALID_SS))
            }

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            verifySuccessfulRegistrationWithRole(UserRole.TUTOR)
        }

    @Test
    fun `GIVEN backend error and firebase delete succeeds WHEN RegisterClicked THEN error is re-thrown`() =
        runTest {
            // GIVEN
            setupRequiredFields()

            val firebaseUser = mockk<FirebaseUser>(relaxed = true)
            val authResult = mockk<AuthResult>(relaxed = true)
            val idTokenResult = mockk<GetTokenResult> {
                every { token } returns TEST_ACCESS_TOKEN
            }

            every { authResult.user } returns firebaseUser
            every {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
            } returns Tasks.forResult(authResult)
            every { firebaseAuth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns TEST_UID
            every { firebaseUser.getIdToken(false) } returns Tasks.forResult(idTokenResult)
            every { firebaseUser.delete() } returns Tasks.forResult(null)

            coEvery { authRepository.saveFirebaseToken(TEST_ACCESS_TOKEN) } returns Unit
            coEvery {
                authRepository.register(any(), VALID_PASSWORD, TEST_ACCESS_TOKEN)
            } throws AppError.ApiError(code = 7003, serverMessage = "DNI already exists")

            // WHEN
            viewModel.handle(RegisterEvent.RegisterClicked)
            advanceUntilIdle()

            // THEN
            assertEquals(
                RegisterGlobalError.BACKEND_DNI_ALREADY_EXISTS,
                viewModel.registerUiState.value.globalError
            )

            verify {
                firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
                firebaseAuth.currentUser
                firebaseUser.uid
                firebaseUser.getIdToken(false)
                firebaseUser.delete()
            }

            coVerify {
                authRepository.saveFirebaseToken(TEST_ACCESS_TOKEN)
                authRepository.register(any(), VALID_PASSWORD, TEST_ACCESS_TOKEN)
            }
        }

    // Helper methods
    private fun setupSuccessfulMocks() {
        val authResult: AuthResult = mockk(relaxed = true)
        val firebaseUser: FirebaseUser = mockk(relaxed = true)
        val idTokenResult: GetTokenResult = mockk(relaxed = true)

        every {
            firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
        } returns Tasks.forResult(authResult)

        every { authResult.user } returns firebaseUser
        every { firebaseUser.uid } returns TEST_UID
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.getIdToken(false) } returns Tasks.forResult(idTokenResult)
        every { idTokenResult.token } returns TEST_ACCESS_TOKEN

        coEvery {
            authRepository.saveFirebaseToken(TEST_ACCESS_TOKEN)
        } returns Unit

        coEvery {
            authRepository.register(any(), any(), any())
        } returns RegisterResult(
            success = true,
            accessToken = TEST_ACCESS_TOKEN,
            refreshToken = TEST_REFRESH_TOKEN
        )
    }

    private fun setupRequiredFields() {
        with(viewModel) {
            handle(RegisterEvent.OnEmailChanged(VALID_EMAIL))
            handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
            handle(RegisterEvent.OnConfirmPasswordChanged(VALID_PASSWORD))
            handle(RegisterEvent.OnNameChanged(VALID_NAME))
            handle(RegisterEvent.OnSurnameChanged(VALID_SURNAME))
            handle(RegisterEvent.OnRoleChanged(UserRole.STUDENT))
        }
    }

    private fun setupRequiredFieldsWithValidation() {
        with(viewModel) {
            handle(RegisterEvent.OnEmailChanged(VALID_EMAIL))
            handle(RegisterEvent.EmailLostFocus)

            handle(RegisterEvent.OnPasswordChanged(VALID_PASSWORD))
            handle(RegisterEvent.PasswordLostFocus)

            handle(RegisterEvent.OnConfirmPasswordChanged(VALID_PASSWORD))
            handle(RegisterEvent.ConfirmPasswordLostFocus)

            handle(RegisterEvent.OnNameChanged(VALID_NAME))
            handle(RegisterEvent.NameLostFocus)

            handle(RegisterEvent.OnSurnameChanged(VALID_SURNAME))
            handle(RegisterEvent.SurnameLostFocus)

            handle(RegisterEvent.OnRoleChanged(UserRole.STUDENT))
            handle(RegisterEvent.RoleLostFocus)
        }
    }

    private fun verifyNoErrorsInOptionalFields() {
        with(viewModel.registerUiState.value) {
            assertEquals(null, dniInput.errorType)
            assertEquals(null, socialSecurityInput.errorType)
            assertEquals(null, contactEmailInput.errorType)
            assertEquals(null, contactPhoneInput.errorType)
            assertEquals(null, contactNameInput.errorType)
        }
    }

    private fun verifySuccessfulRegistrationWithRole(role: UserRole) {
        verify {
            firebaseAuth.createUserWithEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)
            firebaseAuth.currentUser
        }

        coVerify {
            authRepository.saveFirebaseToken(TEST_ACCESS_TOKEN)
            authRepository.register(
                withArg { user ->
                    assertEquals(VALID_EMAIL, user.email)
                    assertEquals(VALID_NAME, user.name)
                    assertEquals(VALID_SURNAME, user.surname)
                    assertEquals(role, user.role)
                },
                eq(VALID_PASSWORD),
                eq(TEST_ACCESS_TOKEN)
            )
        }
    }
}
