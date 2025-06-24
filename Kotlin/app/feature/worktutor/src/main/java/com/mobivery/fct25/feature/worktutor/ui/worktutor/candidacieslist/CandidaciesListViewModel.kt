package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist

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

private const val PAGE_LIMIT = 25

interface CandidaciesListViewModelInterface : BaseViewModelInterface {
    val candidaciesListUiState: StateFlow<CandidaciesListUiState>
    fun handle(event: CandidaciesListEvent)
}

@HiltViewModel
class CandidaciesListViewModel @Inject constructor(
    private val candidacyRepository: CandidacyRepository
) : BaseViewModel(), CandidaciesListViewModelInterface {

    private val _uiState = MutableStateFlow(CandidaciesListUiState())
    override val candidaciesListUiState: StateFlow<CandidaciesListUiState> = _uiState.asStateFlow()

    override fun handle(event: CandidaciesListEvent) = when (event) {
        is CandidaciesListEvent.OnLoadIfNeeded -> loadIfNeeded(event.offerId)
        is CandidaciesListEvent.OnCandidacyClick -> navigateToDetail(event.candidacyId)
        is CandidaciesListEvent.OnScrollPositionChanged -> checkPagination(event.lastVisibleItemIndex)
        CandidaciesListEvent.OnRefresh -> refresh()
        CandidaciesListEvent.DidNavigate -> clearNavigation()
    }

    private fun loadIfNeeded(offerId: Int) {
        if (_uiState.value.candidacies.isEmpty()) {
            fetchCandidacies(offerId)
        } else {
            refresh()
        }
    }

    private fun refresh() {
        _uiState.value.offerId?.let {
            _uiState.update { it.copy(isRefreshing = true, page = 0) }
            fetchCandidacies(it, 0)
        }
    }

    private fun fetchCandidacies(offerId: Int, page: Int = 0) {
        _uiState.update { it.copy(isLoading = true, offerId = offerId, page = page) }

        launchWithErrorWrapper {
            val result = candidacyRepository.getCandidaciesByOfferId(
                offerId = offerId,
                page = page,
                limit = PAGE_LIMIT
            )

            val hasNext = (page + 1) * PAGE_LIMIT < result.total
            val merged = if (page == 0) result.candidacies
            else _uiState.value.candidacies + result.candidacies

            _uiState.update {
                it.copy(
                    candidacies = merged,
                    total = result.total,
                    hasNextPage = hasNext,
                    hasPreviousPage = page > 0,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    private fun navigateToDetail(id: Int) =
        _uiState.update {
            it.copy(
                navigation = CandidaciesListNavigation.NavigateToCandidacyDetail(
                    id
                )
            )
        }

    private fun clearNavigation() =
        _uiState.update { it.copy(navigation = null) }

    private fun checkPagination(lastVisible: Int) {
        val st = _uiState.value
        val threshold = st.candidacies.size - 3
        if (lastVisible >= threshold && st.hasNextPage && !st.isLoading) {
            fetchCandidacies(st.offerId ?: return, st.page + 1)
        }
    }
}

data class CandidaciesListUiState(
    val offerId: Int? = null,
    val candidacies: List<Candidacy> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean = false,
    val navigation: CandidaciesListNavigation? = null
)

sealed interface CandidaciesListNavigation {
    data class NavigateToCandidacyDetail(val candidacyId: Int) : CandidaciesListNavigation
}

sealed interface CandidaciesListEvent {
    data class OnLoadIfNeeded(val offerId: Int) : CandidaciesListEvent
    data class OnCandidacyClick(val candidacyId: Int) : CandidaciesListEvent
    data class OnScrollPositionChanged(val lastVisibleItemIndex: Int) : CandidaciesListEvent
    data object OnRefresh : CandidaciesListEvent
    data object DidNavigate : CandidaciesListEvent
}
