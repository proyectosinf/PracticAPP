package com.mobivery.fct25.ui.main

import androidx.lifecycle.viewModelScope
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.domain.repository.FeatureFlagsRepository
import com.mobivery.fct25.model.SectionUiModel
import com.mobivery.template.domain.model.auth.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainViewModelInterface : BaseViewModelInterface {
    val mainUiState: StateFlow<MainUiState>
    fun handle(event: MainEvent)
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : BaseViewModel(), MainViewModelInterface {

    private val _mainUiState = MutableStateFlow(MainUiState())
    override val mainUiState: StateFlow<MainUiState> = _mainUiState
    private var latestShowAboutTab: Boolean = false

    init {
        observeUserLogged()
        observeUserLocal()
        refreshFirebaseToken()
        listenFeatureFlagsUpdates()
    }

    override fun handle(event: MainEvent) {
        when (event) {
            MainEvent.OnNavigatedToLogin -> {
                _mainUiState.update { it.copy(navigateToLogin = false) }
            }
        }
    }

    private fun observeUserLogged() {
        launchWithErrorWrapper(showLoading = false) {
            authRepository.isUserLogged().collectLatest { isUserLogged ->
                _mainUiState.update { it.copy(isUserLogged = isUserLogged) }
            }
        }
    }

    private fun observeUserLocal() {
        launchWithErrorWrapper(showLoading = false) {
            authRepository.getUserLocal().collectLatest { user ->
                _mainUiState.update { it.copy(userLocal = user, loading = false) }
                buildSections(latestShowAboutTab)
            }
        }
    }

    private fun refreshFirebaseToken() {
        launchWithErrorWrapper(
            showLoading = false,
            showDefaultError = false,
            onError = { error ->
                if (error is AppError.UnauthorizedError) {
                    _mainUiState.update { it.copy(navigateToLogin = true) }
                }
            }
        ) {
            authRepository.refreshTokenIfNeeded()
        }
    }

    private fun listenFeatureFlagsUpdates() {
        viewModelScope.launch {
            featureFlagsRepository.showAboutTabFlow().collectLatest { showAbout ->
                latestShowAboutTab = showAbout
                buildSections(showAbout)
            }
        }
    }

    private fun buildSections(showAboutTab: Boolean) {
        val user = _mainUiState.value.userLocal

        val sectionList = when (user?.role) {
            1 -> listOfNotNull(
                SectionUiModel.StudentOffers,
                SectionUiModel.Candidacies,
            )

            2 -> listOfNotNull(
                SectionUiModel.Company,
                SectionUiModel.Offers,
            )

            else -> listOfNotNull(
                SectionUiModel.Home,
                SectionUiModel.About.takeIf { showAboutTab },
                SectionUiModel.Components
            )
        }

        _mainUiState.update {
            it.copy(sections = sectionList)
        }
    }
}

sealed interface MainEvent {
    object OnNavigatedToLogin : MainEvent
}

data class MainUiState(
    val loading: Boolean = true,
    val isUserLogged: Boolean? = null,
    val userLocal: UserSession? = null,
    val sections: List<SectionUiModel> = listOf(
        SectionUiModel.Home,
        SectionUiModel.About,
        SectionUiModel.Components
    ),
    val navigateToLogin: Boolean = false,
)
