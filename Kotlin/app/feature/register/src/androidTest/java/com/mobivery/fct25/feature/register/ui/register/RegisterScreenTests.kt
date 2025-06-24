package com.mobivery.fct25.feature.register.ui.register

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.feature.register.model.RegisterFormError
import com.mobivery.fct25.feature.register.test.R
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.REGISTERBUTTON
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.EMAIL_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.PASSWORD_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.CONFIRM_PASSWORD_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.NAME_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterTestTags.SURNAME_FIELD
import com.mobivery.fct25.feature.register.ui.register.RegisterViewModel.RegisterUiState
import com.mobivery.template.domain.model.user.UserRole
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterScreenTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @MockK
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { viewModel.loading } returns MutableStateFlow(false)
        every { viewModel.error } returns MutableStateFlow(null)
    }

    @Test
    fun showsRegisterFormElements() {
        // GIVEN
        every { viewModel.registerUiState } returns MutableStateFlow(RegisterUiState())

        // WHEN
        composeTestRule.setContent {
            AppTheme {
                RegisterScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithTag(EMAIL_FIELD).assertExists()
        composeTestRule.onNodeWithTag(PASSWORD_FIELD).assertExists()
        composeTestRule.onNodeWithTag(CONFIRM_PASSWORD_FIELD).assertExists()
        composeTestRule.onNodeWithTag(NAME_FIELD).assertExists()
        composeTestRule.onNodeWithTag(SURNAME_FIELD).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_role_label_text),
            substring = true
        ).assertExists()
    }

    @Test
    fun showsStudentFieldsWhenStudentRoleSelected() {
        // GIVEN
        every { viewModel.registerUiState } returns MutableStateFlow(
            RegisterUiState(selectedRole = UserRole.STUDENT)
        )

        // WHEN
        composeTestRule.setContent {
            AppTheme {
                RegisterScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_dni_label_text)
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_social_security_label_text)
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_contact_name_label_text)
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_contact_email_label_text)
        ).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.register_contact_phone_label_text)
        ).assertExists()
    }

    @Test
    fun showsRegisterButton() {
        // GIVEN
        val uiState = RegisterUiState(isLoading = false)
        every { viewModel.registerUiState } returns MutableStateFlow(uiState)

        // WHEN
        composeTestRule.setContent {
            AppTheme {
                RegisterScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithTag(REGISTERBUTTON).assertExists()
    }

    @Test
    fun showsErrorMessagesWhenPresent() {
        // GIVEN
        every { viewModel.registerUiState } returns MutableStateFlow(
            RegisterUiState(
                emailInput = TextFieldUiModel(
                    text = "",
                    errorType = RegisterFormError.EMPTY
                )
            )
        )

        // WHEN
        composeTestRule.setContent {
            AppTheme {
                RegisterScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // THEN
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(RegisterFormError.EMPTY.messageResourceId)
        ).assertExists()
    }
}
