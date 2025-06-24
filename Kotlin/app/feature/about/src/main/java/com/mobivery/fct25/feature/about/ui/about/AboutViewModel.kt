package com.mobivery.fct25.feature.about.ui.about

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface AboutViewModelInterface : BaseViewModelInterface {
    // Outputs
    val aboutUiState: StateFlow<AboutUiState>

    // Inputs
    fun handle(event: AboutEvent)
}

@HiltViewModel
class AboutViewModel @Inject constructor() : BaseViewModel(), AboutViewModelInterface {

    private val _aboutUiState = MutableStateFlow(AboutUiState())
    override val aboutUiState = _aboutUiState.asStateFlow()

    override fun handle(event: AboutEvent) {
        when (event) {
            AboutEvent.DidNavigate -> didNavigate()
        }
    }

    private fun didNavigate() {
        _aboutUiState.update {
            it.copy(
                navigation = null,
            )
        }
    }
}

data class AboutUiState(
    val navigation: AboutNavigation? = null
)

sealed interface AboutNavigation {
}

sealed interface AboutEvent {
    data object DidNavigate : AboutEvent
}