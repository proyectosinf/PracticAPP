package com.mobivery.fct25.feature.components.ui.componentsdialog

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ComponentsDialogsViewModelInterface : BaseViewModelInterface {
    // Outputs
    val componentsDialogsUiState: StateFlow<ComponentsDialogsUiState>

    // Inputs
    fun handle(event: ComponentsDialogsEvent)
}

@HiltViewModel
class ComponentsDialogsViewModel @Inject constructor(
) : BaseViewModel(), ComponentsDialogsViewModelInterface {

    private val _componentsDialogsUiState = MutableStateFlow(ComponentsDialogsUiState())
    override val componentsDialogsUiState = _componentsDialogsUiState.asStateFlow()

    override fun handle(event: ComponentsDialogsEvent) {
        when (event) {
            ComponentsDialogsEvent.InfoDialog -> {
                _componentsDialogsUiState.update {
                    it.copy(
                        dialog = ComponentsDialogsDialog.InfoDialog
                    )
                }
            }

            ComponentsDialogsEvent.ErrorDialog -> _componentsDialogsUiState.update {
                it.copy(
                    dialog = ComponentsDialogsDialog.ErrorDialog
                )
            }

            ComponentsDialogsEvent.TextInputDialog -> _componentsDialogsUiState.update {
                it.copy(
                    dialog = ComponentsDialogsDialog.TextInputDialog
                )
            }

            ComponentsDialogsEvent.WarningDialog -> _componentsDialogsUiState.update {
                it.copy(
                    dialog = ComponentsDialogsDialog.WarningDialog
                )
            }

            ComponentsDialogsEvent.DismissDialog -> dismissDialog()

            is ComponentsDialogsEvent.SetInputText -> _componentsDialogsUiState.update {
                it.copy(
                    inputText = event.text,
                    dialog = null
                )
            }

            ComponentsDialogsEvent.SnackbarDismissed -> _componentsDialogsUiState.update {
                it.copy(
                    inputText = ""
                )
            }
        }
    }

    private fun dismissDialog() {
        _componentsDialogsUiState.update {
            it.copy(
                dialog = null,
            )
        }
    }
}

data class ComponentsDialogsUiState(
    val inputText: String = "",
    val dialog: ComponentsDialogsDialog? = null,
)

sealed interface ComponentsDialogsDialog {
    data object InfoDialog : ComponentsDialogsDialog
    data object ErrorDialog : ComponentsDialogsDialog
    data object WarningDialog : ComponentsDialogsDialog
    data object TextInputDialog : ComponentsDialogsDialog
}

sealed interface ComponentsDialogsEvent {
    data object InfoDialog : ComponentsDialogsEvent
    data object ErrorDialog : ComponentsDialogsEvent
    data object WarningDialog : ComponentsDialogsEvent
    data object TextInputDialog : ComponentsDialogsEvent
    data object DismissDialog : ComponentsDialogsEvent
    data class SetInputText(val text: String) : ComponentsDialogsEvent
    data object SnackbarDismissed : ComponentsDialogsEvent
}