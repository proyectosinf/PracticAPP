package com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer

import Offer
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.OfferItemUiModel
import com.mobivery.fct25.domain.model.error.AppError
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.StudentPublishedOffersViewModel.Companion.PAGE_LIMIT
import com.mobivery.template.domain.repository.OfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface StudentPublishedOffersViewModelInterface : BaseViewModelInterface {
    val uiState: StateFlow<StudentPublishedOffersUiState>
    fun handle(event: StudentPublishedOffersEvent)
}

@HiltViewModel
class StudentPublishedOffersViewModel @Inject constructor(
    private val offerRepository: OfferRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), StudentPublishedOffersViewModelInterface {

    companion object {
        const val PAGE_LIMIT = 25
        private const val ERROR_CODE_NO_OFFERS = 2001
    }

    private val _uiState = MutableStateFlow(StudentPublishedOffersUiState())
    override val uiState: StateFlow<StudentPublishedOffersUiState> = _uiState.asStateFlow()

    override fun handle(event: StudentPublishedOffersEvent) = when (event) {
        is StudentPublishedOffersEvent.OnOfferClick -> navigate(event.offerId)
        is StudentPublishedOffersEvent.OnOfferRegistered -> updateOfferRegistered(event.offerId)
        is StudentPublishedOffersEvent.OnScrollPositionChanged -> saveScroll(event)
        is StudentPublishedOffersEvent.OnReturnFromDetails -> {
            val updatedList = _uiState.value.offers.map {
                if (it.id == event.offerId) it.copy(isEnrolled = true) else it
            }
            _uiState.update { it.copy(offers = updatedList) }
        }

        StudentPublishedOffersEvent.OnScreenLoad -> initialLoad()
        StudentPublishedOffersEvent.OnPullToRefresh -> refreshOffers()
        StudentPublishedOffersEvent.OnScrollToEnd -> loadNextPageIfNeeded()
        StudentPublishedOffersEvent.OnScrollRestored -> _uiState.update { it.copy(restoreScroll = false) }
        StudentPublishedOffersEvent.RequestLogoutConfirmation -> _uiState.update {
            it.copy(showLogoutConfirmationDialog = true)
        }

        StudentPublishedOffersEvent.CancelLogout -> _uiState.update {
            it.copy(showLogoutConfirmationDialog = false)
        }

        StudentPublishedOffersEvent.ConfirmLogout -> logout()
        StudentPublishedOffersEvent.DidNavigate -> _uiState.update { it.copy(navigation = null) }
    }

    private fun initialLoad() {
        _uiState.value.takeIf { it.offers.isEmpty() && !it.isLoading } ?: return
        fetchOffers(resetPage = true)
    }

    private fun refreshOffers() = fetchOffers(resetPage = true, isPullRefresh = true)

    private fun loadNextPageIfNeeded() {
        val s = _uiState.value
        if (s.isLoadingMore || !s.hasNextPage) return
        _uiState.update { it.copy(isLoadingMore = true) }
        loadOffers(page = s.page + 1, append = true)
    }

    private fun fetchOffers(resetPage: Boolean, isPullRefresh: Boolean = false) {
        _uiState.update {
            it.copy(
                isLoading = resetPage && !isPullRefresh,
                isRefreshing = isPullRefresh,
                error = null,
                restoreScroll = true
            )
        }
        loadOffers(page = 0, append = false)
    }

    private fun loadOffers(page: Int, append: Boolean) {
        launchWithErrorWrapper(
            showDefaultError = false,
            onError = ::handleLoadError
        ) {
            val result = offerRepository.getOffers(page, PAGE_LIMIT)
            val uiItems = result.offers.map { it.toUiModel(context) }
            val merged = if (append) _uiState.value.offers + uiItems else uiItems
            val hasNextPage = (page + 1) * PAGE_LIMIT < result.total

            _uiState.update {
                it.copy(
                    offers = merged,
                    totalOffers = result.total,
                    page = page,
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false,
                    hasNextPage = hasNextPage,
                    hasPreviousPage = page > 0,
                    restoreScroll = !append && it.restoreScroll
                )
            }
        }
    }

    private fun updateOfferRegistered(offerId: Int) {
        _uiState.update { state ->
            state.copy(
                offers = state.offers.map {
                    if (it.id == offerId) it.copy(isEnrolled = true) else it
                }
            )
        }
    }

    private fun saveScroll(e: StudentPublishedOffersEvent.OnScrollPositionChanged) {
        _uiState.update {
            it.copy(
                firstVisibleItemIndex = e.index,
                firstVisibleItemOffset = e.offset
            )
        }
    }

    private fun navigate(offerId: Int) {
        _uiState.update {
            it.copy(
                navigation = StudentPublishedOffersNavigation.NavigateToOfferDetails(offerId)
            )
        }
    }

    private fun logout() {
        _uiState.update { it.copy(showLogoutConfirmationDialog = false) }
        launchWithErrorWrapper(showLoading = false) { authRepository.logout() }
    }

    private fun handleLoadError(t: Throwable) {
        val api = t as? AppError.ApiError
        if (api?.code != ERROR_CODE_NO_OFFERS) error.value = api
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
    }
}

fun Offer.toUiModel(context: Context): OfferItemUiModel = OfferItemUiModel(
    id = id,
    title = title,
    degree = degreeName,
    vacancies = vacancies,
    views = views,
    company = company,
    logoUrl = companyPhoto,
    startDate = startDate,
    endDate = endDate,
    isEnrolled = inscribe == true,
    inscriptionsCandidacy = inscriptionsCandidacy,
    typeLabel = when (type) {
        1 -> context.getString(R.string.offer_type_regular_text)
        2 -> context.getString(R.string.offer_type_adults_text)
        else -> context.getString(R.string.offer_type_unknown_text)
    }
)

data class StudentPublishedOffersUiState(
    val offers: List<OfferItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemOffset: Int = 0,
    val restoreScroll: Boolean = false,
    val page: Int = 0,
    val totalOffers: Int = 0,
    val limitPerPage: Int = PAGE_LIMIT,
    val hasPreviousPage: Boolean = false,
    val hasNextPage: Boolean = false,
    val navigation: StudentPublishedOffersNavigation? = null,
    val showLogoutConfirmationDialog: Boolean = false,
    val error: AppError? = null
)

sealed interface StudentPublishedOffersNavigation {
    data class NavigateToOfferDetails(val offerId: Int) : StudentPublishedOffersNavigation
}

sealed interface StudentPublishedOffersEvent {
    data class OnOfferClick(val offerId: Int) : StudentPublishedOffersEvent
    data class OnOfferRegistered(val offerId: Int) : StudentPublishedOffersEvent
    data class OnReturnFromDetails(val offerId: Int) : StudentPublishedOffersEvent
    data class OnScrollPositionChanged(val index: Int, val offset: Int) :
        StudentPublishedOffersEvent

    data object OnScreenLoad : StudentPublishedOffersEvent
    data object OnPullToRefresh : StudentPublishedOffersEvent
    data object OnScrollToEnd : StudentPublishedOffersEvent
    data object OnScrollRestored : StudentPublishedOffersEvent
    data object RequestLogoutConfirmation : StudentPublishedOffersEvent
    data object CancelLogout : StudentPublishedOffersEvent
    data object ConfirmLogout : StudentPublishedOffersEvent
    data object DidNavigate : StudentPublishedOffersEvent
}
