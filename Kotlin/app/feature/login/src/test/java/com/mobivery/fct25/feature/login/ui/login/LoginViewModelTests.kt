package com.mobivery.fct25.feature.login.ui.login

import com.mobivery.fct25.domain.model.auth.UserCredentials
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.domain.repository.SessionRepository
import com.mobivery.fct25.feature.login.model.LoginFormError
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginEvent
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginNavigation
import com.mobivery.fct25.testing.utils.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MainDispatcherRule::class)
class LoginViewModelTests {

    private companion object {
        const val VALID_USERNAME = "test_user"
        const val VALID_PASSWORD = "secure_password"
        const val EMPTY_STRING = ""
        const val FAKE_ACCESS_TOKEN = "fakeAccessToken"
        const val FAKE_REFRESH_TOKEN = "fakeRefreshToken"
    }

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var sessionRepository: SessionRepository

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        coEvery { authRepository.login(any(), any()) } returns UserCredentials(
            accessToken = FAKE_ACCESS_TOKEN,
            refreshToken = FAKE_REFRESH_TOKEN
        )
        every { sessionRepository.saveTokens(any(), any()) } just runs
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN valid credentials AND user has company WHEN onLoginButtonClick THEN navigates to company`() = runTest {
        // GIVEN
        val userSession = com.mobivery.template.domain.model.auth.UserSession(
            email = "pedro@example.com",
            firebaseUid = "firebaseUid_1",
            role = 2,
            companyId = 123
        )

        coEvery { authRepository.getUserLocal() } returns flowOf(userSession)

        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetUser(VALID_USERNAME))
        viewModel.handle(LoginEvent.SetPassword(VALID_PASSWORD))

        // WHEN
        viewModel.handle(LoginEvent.OnLoginButtonClick)
        advanceUntilIdle()

        // THEN
        coVerify { authRepository.login(VALID_USERNAME, VALID_PASSWORD) }
        assertEquals(LoginNavigation.NavigateToCompany, viewModel.loginUiState.value.navigation)
    }

    @Test
    fun `GIVEN empty credentials WHEN onLoginButtonClick THEN repository is not called and errors are shown`() = runTest {
        // GIVEN
        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetUser(EMPTY_STRING))
        viewModel.handle(LoginEvent.SetPassword(EMPTY_STRING))

        // WHEN
        viewModel.handle(LoginEvent.OnLoginButtonClick)

        // THEN
        coVerify(exactly = 0) { authRepository.login(any(), any()) }
        assertEquals(LoginFormError.EMPTY, viewModel.loginUiState.value.userInput.errorType)
        assertEquals(LoginFormError.EMPTY, viewModel.loginUiState.value.passwordInput.errorType)
    }

    @Test
    fun `GIVEN empty username WHEN user loses focus THEN error is shown`() {
        // GIVEN
        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetUser(EMPTY_STRING))

        // WHEN
        viewModel.handle(LoginEvent.UserLostFocus)

        // THEN
        assertEquals(LoginFormError.EMPTY, viewModel.loginUiState.value.userInput.errorType)
    }

    @Test
    fun `GIVEN valid username WHEN user loses focus THEN no error is shown`() {
        // GIVEN
        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetUser(VALID_USERNAME))

        // WHEN
        viewModel.handle(LoginEvent.UserLostFocus)

        // THEN
        assertEquals(null, viewModel.loginUiState.value.userInput.errorType)
        assertEquals(VALID_USERNAME, viewModel.loginUiState.value.userInput.text)
    }

    @Test
    fun `GIVEN empty password WHEN password loses focus THEN error is shown`() {
        // GIVEN
        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetPassword(EMPTY_STRING))

        // WHEN
        viewModel.handle(LoginEvent.PasswordLostFocus)

        // THEN
        assertEquals(LoginFormError.EMPTY, viewModel.loginUiState.value.passwordInput.errorType)
    }

    @Test
    fun `GIVEN valid password WHEN password loses focus THEN no error is shown`() {
        // GIVEN
        viewModel = LoginViewModel(authRepository, sessionRepository)
        viewModel.handle(LoginEvent.SetPassword(VALID_PASSWORD))

        // WHEN
        viewModel.handle(LoginEvent.PasswordLostFocus)

        // THEN
        assertEquals(null, viewModel.loginUiState.value.passwordInput.errorType)
        assertEquals(VALID_PASSWORD, viewModel.loginUiState.value.passwordInput.text)
    }
}
