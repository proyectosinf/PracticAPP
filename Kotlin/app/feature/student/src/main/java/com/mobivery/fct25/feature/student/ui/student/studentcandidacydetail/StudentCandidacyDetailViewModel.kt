package com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail

import androidx.lifecycle.SavedStateHandle
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.StudentCandidacyDetailViewModel.StudentCandidacyDetailEvent
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.StudentCandidacyDetailViewModel.StudentCandidacyDetailUiState
import com.mobivery.template.domain.model.candidacy.CandidacyDetail
import com.mobivery.template.domain.repository.CandidacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface StudentCandidacyDetailViewModelInterface : BaseViewModelInterface {
    val uiState: StateFlow<StudentCandidacyDetailUiState>
    fun handle(event: StudentCandidacyDetailEvent)
}

@HiltViewModel
class StudentCandidacyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val candidacyRepository: CandidacyRepository
) : BaseViewModel(), StudentCandidacyDetailViewModelInterface {

    private val candidacyId: Int = checkNotNull(savedStateHandle.get<Int>("candidacyId")) {
        "Missing candidacyId"
    }

    private val _uiState = MutableStateFlow(StudentCandidacyDetailUiState())
    override val uiState: StateFlow<StudentCandidacyDetailUiState> = _uiState.asStateFlow()

    init {
        loadCandidacy()
    }

    override fun handle(event: StudentCandidacyDetailEvent) {
        when (event) {
            StudentCandidacyDetailEvent.OnBackClick -> {
                _uiState.update { it.copy(navigation = StudentCandidacyDetailNavigation.NavigateBack) }
            }

            StudentCandidacyDetailEvent.OnNavigationHandled -> {
                _uiState.update { it.copy(navigation = null) }
            }
        }
    }

    private fun loadCandidacy() {
        launchWithErrorWrapper {
            val detail = candidacyRepository.getCandidacyById(candidacyId)
            _uiState.update {
                it.copy(candidacy = detail)
            }
        }
    }

    data class StudentCandidacyDetailUiState(
        val candidacy: CandidacyDetail? = null,
        val navigation: StudentCandidacyDetailNavigation? = null
    )

    sealed interface StudentCandidacyDetailEvent {
        object OnBackClick : StudentCandidacyDetailEvent
        object OnNavigationHandled : StudentCandidacyDetailEvent
    }

    sealed interface StudentCandidacyDetailNavigation {
        object NavigateBack : StudentCandidacyDetailNavigation
    }
}
