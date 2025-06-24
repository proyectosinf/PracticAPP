package com.mobivery.fct25.feature.home.ui.home

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface HomeViewModelInterface : BaseViewModelInterface {
    // Outputs
    val homeUiState: StateFlow<HomeUiState>

    // Inputs
    fun handle(event: HomeEvent)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel(), HomeViewModelInterface {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    override val homeUiState = _homeUiState.asStateFlow()

    override fun handle(event: HomeEvent) {
        when (event) {
            HomeEvent.Logout -> logout()
            HomeEvent.DidNavigate -> didNavigate()
        }
    }

    private fun logout() {
        launchWithErrorWrapper {
            authRepository.logout()
        }
    }

    private fun didNavigate() {
        _homeUiState.update {
            it.copy(
                navigation = null,
            )
        }
    }

}

data class HomeUiState(
    val navigation: HomeNavigation? = null
)

sealed interface HomeNavigation {
}

sealed interface HomeEvent {
    data object Logout : HomeEvent
    data object DidNavigate : HomeEvent
}