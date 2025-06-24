package com.mobivery.fct25

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.domain.repository.FeatureFlagsRepository
import com.mobivery.fct25.domain.usecases.featureflags.UpdateAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface RootViewModelInterface : BaseViewModelInterface {
    // Outputs
    val rootUiState: StateFlow<RootUiState>

    // Inputs
    fun handle(event: RootEvent)
}

@HiltViewModel
class RootViewModel @Inject constructor(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val updateAppUseCase: UpdateAppUseCase,
) : BaseViewModel(), RootViewModelInterface {

    private val _rootUiState = MutableStateFlow(RootUiState())
    override val rootUiState: StateFlow<RootUiState> = _rootUiState

    init {
        initFeatureFlagsAndCheckUpdateAppNeeded()
    }

    override fun handle(event: RootEvent) {
        when (event) {
            RootEvent.InAppUpdateCancelled -> inAppUpdateCancelled()
            RootEvent.InAppUpdateError -> inAppUpdateError()
            RootEvent.InAppUpdateDownloaded -> _rootUiState.update { it.copy(dialog = RootDialog.UpdateDownloadedDialog) }
            RootEvent.InAppUpdateInstall -> _rootUiState.update {
                it.copy(
                    installDownloadedUpdate = true,
                    dialog = null
                )
            }

            RootEvent.DismissUpdateDialog -> {
                if (_rootUiState.value.forceUpdate) {
                    _rootUiState.update { it.copy(navigation = RootNavigation.ExitApp) }
                } else {
                    _rootUiState.update {
                        it.copy(dialog = null)
                    }
                }
            }

            RootEvent.DismissDialog -> _rootUiState.update { it.copy(dialog = null) }
        }
    }

    private fun initFeatureFlagsAndCheckUpdateAppNeeded() {
        launchWithErrorWrapper(
            showDefaultError = false,
            showLoading = false
        ) {
            featureFlagsRepository.init()
            // Waits until the feature flags have been loaded to get their current value
            with(updateAppUseCase()) {
                _rootUiState.update {
                    it.copy(
                        updateApp = this.isActive ?: false,
                        forceUpdate = this.force ?: false
                    )
                }
            }
        }
    }

    private fun inAppUpdateCancelled() {
        if (rootUiState.value.forceUpdate) {
            _rootUiState.update { it.copy(navigation = RootNavigation.ExitApp) }
        }
    }

    private fun inAppUpdateError() {
        _rootUiState.update { it.copy(dialog = RootDialog.RetryUpdateDialog) }
    }
}

data class RootUiState(
    val darkMode: Boolean = false,
    val updateApp: Boolean = false,
    val installDownloadedUpdate: Boolean = false,
    val forceUpdate: Boolean = false,
    val dialog: RootDialog? = null,
    val navigation: RootNavigation? = null,
)

sealed interface RootNavigation {
    data object ExitApp : RootNavigation
}

sealed interface RootEvent {
    data object InAppUpdateCancelled : RootEvent
    data object InAppUpdateError : RootEvent
    data object InAppUpdateDownloaded : RootEvent
    data object InAppUpdateInstall : RootEvent
    data object DismissDialog : RootEvent
    data object DismissUpdateDialog : RootEvent
}

sealed interface RootDialog {
    data object RetryUpdateDialog : RootDialog
    data object UpdateDownloadedDialog : RootDialog
}
