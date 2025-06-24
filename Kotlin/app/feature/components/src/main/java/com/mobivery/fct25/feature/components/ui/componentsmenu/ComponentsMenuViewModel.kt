package com.mobivery.fct25.feature.components.ui.componentsmenu

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ComponentsMenuViewModelInterface : BaseViewModelInterface {
    // Outputs
    val componentsMenuUiState: StateFlow<ComponentsMenuUiState>

    // Inputs
    fun handle(event: ComponentsMenuEvent)
}

@HiltViewModel
class ComponentsMenuViewModel @Inject constructor(
) : BaseViewModel(), ComponentsMenuViewModelInterface {

    private val _componentsMenuUiState = MutableStateFlow(ComponentsMenuUiState())
    override val componentsMenuUiState = _componentsMenuUiState.asStateFlow()

    override fun handle(event: ComponentsMenuEvent) {
        when (event) {
            ComponentsMenuEvent.ClickOnButtons -> _componentsMenuUiState.update { it.copy(navigation = ComponentsMenuNavigation.NavigateToButtons) }
            ComponentsMenuEvent.ClickOnDialogs -> _componentsMenuUiState.update { it.copy(navigation = ComponentsMenuNavigation.NavigateToDialogs) }
            ComponentsMenuEvent.ClickOnTextFields -> _componentsMenuUiState.update { it.copy(navigation = ComponentsMenuNavigation.NavigateToTextFields) }
            ComponentsMenuEvent.DidNavigate -> didNavigate()
        }
    }

    private fun didNavigate() {
        if (_componentsMenuUiState.value.navigation != null) {
            _componentsMenuUiState.update {
                it.copy(
                    navigation = null,
                )
            }
        }
    }
}

data class ComponentsMenuUiState(
    val navigation: ComponentsMenuNavigation? = null,
)

sealed interface ComponentsMenuNavigation {
    data object NavigateToButtons : ComponentsMenuNavigation
    data object NavigateToTextFields : ComponentsMenuNavigation
    data object NavigateToDialogs : ComponentsMenuNavigation
}

sealed interface ComponentsMenuEvent {
    data object ClickOnButtons : ComponentsMenuEvent
    data object ClickOnTextFields : ComponentsMenuEvent
    data object ClickOnDialogs : ComponentsMenuEvent
    data object DidNavigate : ComponentsMenuEvent
}