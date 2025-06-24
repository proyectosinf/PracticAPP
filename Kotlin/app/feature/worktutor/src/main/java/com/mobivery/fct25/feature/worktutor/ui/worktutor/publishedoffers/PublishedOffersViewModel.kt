package com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers

import Offer
import android.content.Context
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.OfferItemUiModel
import com.mobivery.fct25.app.common.model.OfferStatus
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.template.domain.repository.OfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface PublishedOffersViewModelInterface : BaseViewModelInterface {
    val publishedOffersUiState: StateFlow<PublishedOffersUiState>
    fun handle(event: PublishedOffersEvent)
}

@HiltViewModel
class PublishedOffersViewModel @Inject constructor(
    private val offerRepository: OfferRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel(), PublishedOffersViewModelInterface {

    companion object {
        const val PAGE_LIMIT = 25
        private const val DAYS_BEFORE_CLOSED_STATUS = 15
        private const val ERROR_CODE_NO_OFFERS = 2001
    }

    private val _publishedOffersUiState = MutableStateFlow(PublishedOffersUiState())
    override val publishedOffersUiState: StateFlow<PublishedOffersUiState> =
        _publishedOffersUiState.asStateFlow()

    override fun handle(event: PublishedOffersEvent) {
        when (event) {
            is PublishedOffersEvent.OnScreenLoad -> handleInitialLoad()
            is PublishedOffersEvent.OnReload -> fetchOffers()
            is PublishedOffersEvent.OnRefresh -> refreshOffers()
            is PublishedOffersEvent.OnScrollToEnd -> loadNextPageIfNeeded()
            is PublishedOffersEvent.OnStatusFilterChange -> updateStatusFilter(event.status)
            is PublishedOffersEvent.OnCreateOfferClick -> navigateToCreateOffer()
            is PublishedOffersEvent.OnCandidacyClick -> navigateToCandidacies(event.offerId)

            is PublishedOffersEvent.OnScrollPositionChanged -> {
                _publishedOffersUiState.update {
                    it.copy(
                        firstVisibleItemIndex = event.index,
                        firstVisibleItemOffset = event.offset
                    )
                }
            }

            is PublishedOffersEvent.OnScrollRestored -> {
                _publishedOffersUiState.update { it.copy(restoreScroll = false) }
            }

            is PublishedOffersEvent.DidNavigate -> clearNavigation()
        }
    }

    private fun handleInitialLoad() {
        val state = _publishedOffersUiState.value
        if (state.offers.isEmpty() && !state.isLoading && !state.isRefreshing) {
            fetchOffers()
        }
    }

    private fun fetchOffers(page: Int = 0, append: Boolean = false) {
        _publishedOffersUiState.update {
            if (append) {
                it
            } else {
                it.copy(
                    isLoading = true,
                    page = page,
                    restoreScroll = true,
                    firstVisibleItemIndex = it.firstVisibleItemIndex,
                    firstVisibleItemOffset = it.firstVisibleItemOffset
                )
            }
        }
        loadOffers(page = page, isRefresh = false, append = append)
    }

    private fun refreshOffers() {
        _publishedOffersUiState.update { it.copy(isRefreshing = true, page = 0) }
        loadOffers(page = 0, isRefresh = true, append = false)
    }

    private fun loadNextPageIfNeeded() {
        val state = _publishedOffersUiState.value
        if (state.isLoadingMore || !state.hasNextPage) return
        _publishedOffersUiState.update { it.copy(isLoadingMore = true) }
        fetchOffers(page = state.page + 1, append = true)
    }

    private fun loadOffers(page: Int, isRefresh: Boolean, append: Boolean) {
        launchWithErrorWrapper(
            showDefaultError = false,
            onError = { error ->
                val apiError = error as? AppError.ApiError
                if (apiError?.code != ERROR_CODE_NO_OFFERS) {
                    this.error.value = error
                }
                _publishedOffersUiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false
                    )
                }
            }
        ) {
            val result = offerRepository.getOffers(page = page, limit = PAGE_LIMIT)
            val newItems = result.offers.map { it.toOfferItem(context) }
            val combinedItems =
                if (append) publishedOffersUiState.value.allOffers + newItems else newItems
            val hasNext = (page + 1) * PAGE_LIMIT < result.total

            _publishedOffersUiState.update {
                it.copy(
                    allOffers = combinedItems,
                    offers = combinedItems.filterByStatus(it.statusFilter),
                    totalOffers = result.total,
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    hasNextPage = hasNext,
                    page = page
                )
            }
        }
    }

    private fun updateStatusFilter(status: OfferStatus) {
        _publishedOffersUiState.update {
            it.copy(
                statusFilter = status,
                offers = it.allOffers.filterByStatus(status)
            )
        }
    }

    private fun navigateToCreateOffer() {
        _publishedOffersUiState.update {
            it.copy(navigation = PublishedOffersNavigation.NavigateToCreateOffer)
        }
    }

    private fun navigateToCandidacies(offerId: String) {
        _publishedOffersUiState.update {
            it.copy(navigation = PublishedOffersNavigation.NavigateToCandidacies(offerId))
        }
    }

    private fun clearNavigation() {
        _publishedOffersUiState.update {
            it.copy(navigation = null)
        }
    }

    private fun Offer.toOfferItem(context: Context): OfferItemUiModel {
        val daysUntilStart = ChronoUnit.DAYS.between(LocalDate.now(), startDate)
        val status = if (daysUntilStart < DAYS_BEFORE_CLOSED_STATUS) {
            OfferStatus.CLOSED
        } else {
            OfferStatus.ACTIVE
        }

        val typeLabel = when (type) {
            1 -> context.getString(R.string.offer_type_regular_text)
            2 -> context.getString(R.string.offer_type_adults_text)
            else -> context.getString(R.string.offer_type_unknown_text)
        }

        return OfferItemUiModel(
            id = id,
            title = title,
            degree = degreeName,
            vacancies = vacancies,
            views = views,
            status = status,
            inscriptionsCandidacy = inscriptionsCandidacy,
            typeLabel = typeLabel
        )
    }

    private fun List<OfferItemUiModel>.filterByStatus(status: OfferStatus): List<OfferItemUiModel> {
        return filter { it.status == status }
    }
}

data class PublishedOffersUiState(
    val allOffers: List<OfferItemUiModel> = emptyList(),
    val offers: List<OfferItemUiModel> = emptyList(),
    val statusFilter: OfferStatus = OfferStatus.ACTIVE,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val page: Int = 0,
    val totalOffers: Int = 0,
    val limitPerPage: Int = PublishedOffersViewModel.PAGE_LIMIT,
    val hasNextPage: Boolean = false,
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemOffset: Int = 0,
    val restoreScroll: Boolean = false,
    val navigation: PublishedOffersNavigation? = null
)

sealed interface PublishedOffersNavigation {
    data object NavigateToCreateOffer : PublishedOffersNavigation
    data class NavigateToCandidacies(val offerId: String) : PublishedOffersNavigation
}

sealed interface PublishedOffersEvent {
    data class OnCandidacyClick(val offerId: String) : PublishedOffersEvent
    data class OnStatusFilterChange(val status: OfferStatus) : PublishedOffersEvent
    data class OnScrollPositionChanged(val index: Int, val offset: Int) : PublishedOffersEvent
    data object OnScrollRestored : PublishedOffersEvent
    data object OnReload : PublishedOffersEvent
    data object OnRefresh : PublishedOffersEvent
    data object OnCreateOfferClick : PublishedOffersEvent
    data object OnScreenLoad : PublishedOffersEvent
    data object DidNavigate : PublishedOffersEvent
    data object OnScrollToEnd : PublishedOffersEvent
}
