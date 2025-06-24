package com.mobivery.fct25.feature.student.ui.student.studentcandidacies

import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.template.domain.model.candidacy.Candidacy
import com.mobivery.template.domain.repository.CandidacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface StudentCandidaciesViewModelInterface : BaseViewModelInterface {
    val studentCandidaciesUiState: StateFlow<StudentCandidaciesUiState>
    fun handle(event: StudentCandidaciesEvent)
}

@HiltViewModel
class StudentCandidaciesViewModel @Inject constructor(
    private val candidacyRepository: CandidacyRepository
) : BaseViewModel(), StudentCandidaciesViewModelInterface {

    companion object {
        private const val PAGE_LIMIT = 25
    }

    private val _uiState = MutableStateFlow(StudentCandidaciesUiState())
    override val studentCandidaciesUiState: StateFlow<StudentCandidaciesUiState> =
        _uiState.asStateFlow()

    override fun handle(event: StudentCandidaciesEvent) {
        when (event) {
            StudentCandidaciesEvent.OnForceRefresh -> fetchCandidacies(forceRefresh = true)

            StudentCandidaciesEvent.OnScrollToEnd -> loadMoreCandidacies()

            StudentCandidaciesEvent.OnRetryClick -> fetchCandidacies(forceRefresh = true)

            is StudentCandidaciesEvent.OnCandidacyClick -> navigateToCandidacy(event.candidacyId)

            StudentCandidaciesEvent.OnPullToRefresh -> fetchCandidacies(
                forceRefresh = true,
                isPullRefresh = true
            )

            StudentCandidaciesEvent.OnScrollRestored -> _uiState.update { it.copy(restoreScroll = false) }

            is StudentCandidaciesEvent.OnScrollPositionChanged -> {
                _uiState.update {
                    it.copy(
                        firstVisibleItemIndex = event.index,
                        firstVisibleItemOffset = event.offset
                    )
                }
            }

            StudentCandidaciesEvent.DidNavigate -> clearNavigation()
        }
    }

    private fun fetchCandidacies(
        page: Int = 0,
        append: Boolean = false,
        forceRefresh: Boolean = false,
        isPullRefresh: Boolean = false
    ) {
        _uiState.update {
            when {
                append -> it.copy(isLoadingMore = true, error = null)
                isPullRefresh -> it.copy(isRefreshing = true, error = null, restoreScroll = true)
                else -> it.copy(isLoading = true, error = null, restoreScroll = true)
            }.let { updated ->
                if (forceRefresh) updated.copy(candidacies = emptyList()) else updated
            }
        }

        launchWithErrorWrapper(showDefaultError = false, onError = { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    error = error
                )
            }
        }) {
            val result = candidacyRepository.getUserCandidacies(page = page, limit = PAGE_LIMIT)
            val hasNext = (page + 1) * PAGE_LIMIT < result.total
            _uiState.update {
                it.copy(
                    candidacies = if (append) it.candidacies + result.candidacies else result.candidacies,
                    total = result.total,
                    page = page,
                    hasNextPage = hasNext,
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    restoreScroll = !append && it.restoreScroll,
                    hasLoadedOnce = true
                )
            }
        }
    }

    private fun loadMoreCandidacies() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) return
        fetchCandidacies(page = state.page + 1, append = true)
    }

    private fun navigateToCandidacy(candidacyId: Int) {
        _uiState.update {
            it.copy(
                navigation = StudentCandidaciesNavigation.NavigateToCandidacyDetail(candidacyId)
            )
        }
    }

    private fun clearNavigation() {
        _uiState.update { it.copy(navigation = null) }
    }
}

data class StudentCandidaciesUiState(
    val candidacies: List<Candidacy> = emptyList(),
    val page: Int = 0,
    val total: Int = 0,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemOffset: Int = 0,
    val restoreScroll: Boolean = false,
    val hasLoadedOnce: Boolean = false,
    val error: Throwable? = null,
    val navigation: StudentCandidaciesNavigation? = null
)

sealed interface StudentCandidaciesNavigation {
    data class NavigateToCandidacyDetail(val candidacyId: Int) : StudentCandidaciesNavigation
}

sealed interface StudentCandidaciesEvent {
    data class OnCandidacyClick(val candidacyId: Int) : StudentCandidaciesEvent
    data object OnPullToRefresh : StudentCandidaciesEvent
    data object OnScrollRestored : StudentCandidaciesEvent
    data class OnScrollPositionChanged(val index: Int, val offset: Int) : StudentCandidaciesEvent
    data object OnRetryClick : StudentCandidaciesEvent
    data object OnForceRefresh : StudentCandidaciesEvent
    data object OnScrollToEnd : StudentCandidaciesEvent
    data object DidNavigate : StudentCandidaciesEvent
}
