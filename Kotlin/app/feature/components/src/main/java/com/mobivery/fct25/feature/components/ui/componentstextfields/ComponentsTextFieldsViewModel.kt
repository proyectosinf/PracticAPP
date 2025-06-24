package com.mobivery.fct25.feature.components.ui.componentstextfields

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.designsystem.component.textinput.TextFieldWithSuggestionState
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.feature.components.model.ComponentFormError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ComponentsTextFieldsViewModelInterface : BaseViewModelInterface {
    // Outputs
    val componentsTextFieldsUiState: StateFlow<ComponentsTextFieldsUiState>

    // Inputs
    fun handle(event: ComponentsTextFieldsEvent)
}

@HiltViewModel
class ComponentsTextFieldsViewModel @Inject constructor(
) : BaseViewModel(), ComponentsTextFieldsViewModelInterface {

    private val _componentsTextFieldsUiState = MutableStateFlow(ComponentsTextFieldsUiState())
    override val componentsTextFieldsUiState = _componentsTextFieldsUiState.asStateFlow()

    private val suggestionTextList =
        listOf("Alex", "Ana", "David", "Eva", "Gabriel", "Isabel", "Leo", "Oscar", "Sara", "Samuel")

    override fun handle(event: ComponentsTextFieldsEvent) {
        when (event) {
            is ComponentsTextFieldsEvent.OnDefaultTextFieldChange -> {
                _componentsTextFieldsUiState.update {
                    it.copy(defaultTextField = TextFieldUiModel(text = event.value))
                }
            }

            is ComponentsTextFieldsEvent.OnErrorTextFieldChange -> {
                _componentsTextFieldsUiState.update {
                    it.copy(errorTextField = it.errorTextField.copy(text = event.value))
                }
            }

            is ComponentsTextFieldsEvent.OnNumberTextFieldChange -> {
                if (event.value.all { it.isDigit() }) {
                    _componentsTextFieldsUiState.update {
                        it.copy(numberTextField = TextFieldUiModel(text = event.value))
                    }
                }
            }

            is ComponentsTextFieldsEvent.OnTrailingIconTextFieldChange -> {
                _componentsTextFieldsUiState.update {
                    it.copy(trailingIconTextField = TextFieldUiModel(text = event.value))
                }
            }

            is ComponentsTextFieldsEvent.OnPasswordTextFieldChange -> {
                _componentsTextFieldsUiState.update {
                    it.copy(passwordTextField = TextFieldUiModel(text = event.value))
                }
            }

            is ComponentsTextFieldsEvent.OnTextFieldWithSuggestionsChange -> {
                onTextFieldWithSuggestionsChange(event.value)
            }

            is ComponentsTextFieldsEvent.OnSuggestionSelected -> {
                _componentsTextFieldsUiState.update {
                    it.copy(textFieldWithSuggestions = TextFieldWithSuggestionState(event.valueSelected))
                }
            }
        }
    }

    private fun onTextFieldWithSuggestionsChange(value: String) {
        _componentsTextFieldsUiState.update {
            it.copy(textFieldWithSuggestions = TextFieldWithSuggestionState(
                text = value,
                suggestions = value.takeIf { it.isNotBlank() }?.let { prefix ->
                    suggestionTextList.filter { name ->
                        name.startsWith(
                            prefix = prefix,
                            ignoreCase = true
                        )
                    }
                } ?: emptyList()
            ))
        }
    }
}

data class ComponentsTextFieldsUiState(
    val defaultTextField: TextFieldUiModel<ComponentFormError> = TextFieldUiModel(text = ""),
    val errorTextField: TextFieldUiModel<ComponentFormError> = TextFieldUiModel(text = "", errorType = ComponentFormError.FORMAT),
    val numberTextField: TextFieldUiModel<ComponentFormError> = TextFieldUiModel(text = ""),
    val trailingIconTextField: TextFieldUiModel<ComponentFormError> = TextFieldUiModel(text = ""),
    val passwordTextField: TextFieldUiModel<ComponentFormError> = TextFieldUiModel(text = ""),
    val textFieldWithSuggestions: TextFieldWithSuggestionState = TextFieldWithSuggestionState(""),
)

sealed interface ComponentsTextFieldsEvent {
    data class OnDefaultTextFieldChange(val value: String) : ComponentsTextFieldsEvent
    data class OnErrorTextFieldChange(val value: String) : ComponentsTextFieldsEvent
    data class OnNumberTextFieldChange(val value: String) : ComponentsTextFieldsEvent
    data class OnTrailingIconTextFieldChange(val value: String) : ComponentsTextFieldsEvent
    data class OnPasswordTextFieldChange(val value: String) : ComponentsTextFieldsEvent
    data class OnTextFieldWithSuggestionsChange(val value: String) : ComponentsTextFieldsEvent
    data class OnSuggestionSelected(val valueSelected: String) : ComponentsTextFieldsEvent
}