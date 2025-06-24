package com.mobivery.fct25.feature.components.ui.componentstextfields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.PasswordTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.TextFieldWithSuggestion
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.components.R
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsTextFieldsScreen(
    onBack: () -> Unit,
    viewModel: ComponentsTextFieldsViewModelInterface = hiltViewModel<ComponentsTextFieldsViewModel>(),
) {
    val uiState by viewModel.componentsTextFieldsUiState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.components_sample_menu_textfields_button))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(SPACING_L)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            CustomTextField(
                value = uiState.defaultTextField.text,
                label = stringResource(R.string.components_sample_textfields_default_label),
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnDefaultTextFieldChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            CustomTextField(
                value = uiState.errorTextField.text,
                label = stringResource(R.string.components_sample_textfields_error_label),
                isError = uiState.errorTextField.errorType != null,
                errorMessage = uiState.errorTextField.errorType?.messageResourceId?.let {
                    stringResource(it)
                } ?: "",
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnErrorTextFieldChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            CustomTextField(
                value = uiState.numberTextField.text,
                label = stringResource(R.string.components_sample_textfields_number_label),
                placeholder = stringResource(R.string.components_sample_textfields_number_placeholder),
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnNumberTextFieldChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
            CustomTextField(
                value = stringResource(R.string.components_sample_textfields_disabled_label),
                label = stringResource(R.string.components_sample_textfields_disabled_label),
                onValueChange = {
                    // Disabled textField
                },
                isEnabled = false,
                modifier = Modifier.fillMaxWidth(),
            )

            CustomTextField(
                value = uiState.trailingIconTextField.text,
                label = stringResource(R.string.components_sample_textfields_trailing_icon_label),
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnTrailingIconTextFieldChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Rounded.ShoppingCart,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            PasswordTextField(
                value = uiState.passwordTextField.text,
                label = stringResource(R.string.components_sample_textfields_password_label),
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnPasswordTextFieldChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )

            TextFieldWithSuggestion(
                label = stringResource(R.string.components_sample_textfields_suggestions_label),
                value = uiState.textFieldWithSuggestions.text,
                onValueChange = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnTextFieldWithSuggestionsChange(it))
                },
                options = uiState.textFieldWithSuggestions.suggestions,
                onSuggestionSelected = {
                    viewModel.handle(ComponentsTextFieldsEvent.OnSuggestionSelected(it))
                }
            )
        }
    }
}

@Preview
@Composable
private fun ComponentsTextFieldsScreenPreview() {
    AppTheme {
        ComponentsTextFieldsScreen(
            onBack = { },
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : ComponentsTextFieldsViewModelInterface {
        // Outputs
        override val componentsTextFieldsUiState = MutableStateFlow(ComponentsTextFieldsUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: ComponentsTextFieldsEvent) {}
    }
}