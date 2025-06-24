package com.mobivery.fct25.feature.login.ui.login

import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow

class LoginScreenScreenshots {

    private val composePreviewViewModel by lazy {
        object : LoginViewModelInterface {
            override val loginUiState = MutableStateFlow(LoginUiState())
            override val loading = MutableStateFlow(false)
            override val error = MutableStateFlow(null)

            override fun handle(event: LoginEvent) {}
            override fun closeError() {}
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LoginPreview() {
        AppTheme {
            LoginScreen(
                onExitApp = {},
                viewModel = composePreviewViewModel
            )
        }
    }

}