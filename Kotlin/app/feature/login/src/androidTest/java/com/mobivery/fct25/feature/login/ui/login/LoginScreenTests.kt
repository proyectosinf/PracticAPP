package com.mobivery.fct25.feature.login.ui.login

import com.mobivery.fct25.feature.login.R
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import io.mockk.MockKAnnotations
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.feature.login.ui.login.LoginViewModel.LoginUiState
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @MockK
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { viewModel.loading } returns MutableStateFlow(false)
        every { viewModel.error } returns MutableStateFlow(null)
    }

    @Test
    fun showsLoginButton() {
        // GIVEN
        every { viewModel.loginUiState } returns MutableStateFlow(LoginUiState())

        composeTestRule.setContent {
            AppTheme {
                LoginScreen(
                    navController = rememberNavController(),
                    onExitApp = {},
                    viewModel = viewModel,
                    onNavigateToRegister = {},
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.login_send_button),
            ignoreCase = true
        ).assertExists()
    }
}