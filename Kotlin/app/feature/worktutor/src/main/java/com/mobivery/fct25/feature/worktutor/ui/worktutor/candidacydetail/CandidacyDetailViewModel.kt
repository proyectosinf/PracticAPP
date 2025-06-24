package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail

import androidx.lifecycle.SavedStateHandle
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.CandidacyDetailViewModel.CandidacyDetailEvent
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.CandidacyDetailViewModel.CandidacyDetailUiState
import com.mobivery.template.domain.model.candidacy.CandidacyDetail
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import com.mobivery.template.domain.repository.CandidacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface CandidacyDetailViewModelInterface : BaseViewModelInterface {
    val uiState: StateFlow<CandidacyDetailUiState>
    fun handle(event: CandidacyDetailEvent)
}

@HiltViewModel
class CandidacyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val candidacyRepository: CandidacyRepository
) : BaseViewModel(), CandidacyDetailViewModelInterface {

    private val candidacyId: Int = checkNotNull(savedStateHandle["candidacyId"]) {
        "Missing candidacyId"
    }

    private val _uiState = MutableStateFlow(CandidacyDetailUiState())
    override val uiState: StateFlow<CandidacyDetailUiState> = _uiState.asStateFlow()

    init {
        loadCandidacy()
    }

    override fun handle(event: CandidacyDetailEvent) {
        when (event) {
            is CandidacyDetailEvent.OnNoteChange -> _uiState.update {
                it.copy(note = event.value)
            }

            CandidacyDetailEvent.OnAcceptClick -> {
                _uiState.update { it.copy(showAcceptDialog = true) }
            }

            CandidacyDetailEvent.OnRejectClick -> {
                _uiState.update { it.copy(showRejectDialog = true) }
            }

            CandidacyDetailEvent.OnDismissDialog -> {
                _uiState.update {
                    it.copy(showAcceptDialog = false, showRejectDialog = false)
                }
            }

            CandidacyDetailEvent.OnConfirmAccept -> {
                _uiState.update {
                    it.copy(
                        showAcceptDialog = false,
                        alreadyHandled = true
                    )
                }
                updateCandidacyStatus(CandidacyStatus.ACCEPTED)
            }

            CandidacyDetailEvent.OnConfirmReject -> {
                _uiState.update {
                    it.copy(
                        showRejectDialog = false,
                        alreadyHandled = true
                    )
                }
                updateCandidacyStatus(CandidacyStatus.REJECTED)
            }

            CandidacyDetailEvent.OnBackClick -> {
                _uiState.update { it.copy(navigation = CandidacyDetailNavigation.NavigateBack) }
            }

            CandidacyDetailEvent.OnNavigationHandled -> {
                _uiState.update { it.copy(navigation = null) }
            }
        }
    }

    private fun updateCandidacyStatus(newStatus: CandidacyStatus) {
        val candidacy = uiState.value.candidacy ?: return
        if (candidacy.status == newStatus) return

        launchWithErrorWrapper {
            candidacyRepository.updateCandidacyState(
                id = candidacy.id,
                status = newStatus,
                additionalNotes = uiState.value.note
            )

            _uiState.update {
                it.copy(
                    candidacy = it.candidacy?.copy(status = newStatus),
                    alreadyHandled = true
                )
            }
        }
    }

    private fun loadCandidacy() {
        launchWithErrorWrapper {
            val detail = candidacyRepository.getCandidacyById(candidacyId)
            _uiState.update {
                it.copy(
                    candidacy = detail,
                    alreadyHandled = detail.status != CandidacyStatus.PENDING
                )
            }
        }
    }

    data class CandidacyDetailUiState(
        val candidacy: CandidacyDetail? = null,
        val note: String = "",
        val showAcceptDialog: Boolean = false,
        val showRejectDialog: Boolean = false,
        val alreadyHandled: Boolean = false,
        val navigation: CandidacyDetailNavigation? = null
    )

    sealed interface CandidacyDetailEvent {
        object OnAcceptClick : CandidacyDetailEvent
        object OnRejectClick : CandidacyDetailEvent
        object OnConfirmAccept : CandidacyDetailEvent
        object OnConfirmReject : CandidacyDetailEvent
        object OnDismissDialog : CandidacyDetailEvent
        object OnBackClick : CandidacyDetailEvent
        object OnNavigationHandled : CandidacyDetailEvent
        data class OnNoteChange(val value: String) : CandidacyDetailEvent
    }

    sealed interface CandidacyDetailNavigation {
        object NavigateBack : CandidacyDetailNavigation
    }
}
