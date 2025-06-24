package com.mobivery.fct25.feature.student.ui.student.studentofferdetails

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.StudentOfferDetailViewModel.StudentOfferDetailEvent
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.StudentOfferDetailViewModel.StudentOfferDetailUiState
import com.mobivery.template.domain.model.offer.OfferType
import com.mobivery.template.domain.repository.OfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

interface StudentOfferDetailViewModelInterface : BaseViewModelInterface {
    val studentOfferDetailUiState: StateFlow<StudentOfferDetailUiState>
    fun handle(event: StudentOfferDetailEvent)
}

@HiltViewModel
class StudentOfferDetailViewModel @Inject constructor(
    private val offerRepository: OfferRepository
) : BaseViewModel(), StudentOfferDetailViewModelInterface {

    private val _uiState = MutableStateFlow(StudentOfferDetailUiState())
    override val studentOfferDetailUiState: StateFlow<StudentOfferDetailUiState> =
        _uiState.asStateFlow()

    override fun handle(event: StudentOfferDetailEvent) {
        when (event) {
            is StudentOfferDetailEvent.OnLoadOffer -> loadOffer(event.offerId)

            is StudentOfferDetailEvent.OnRegisterClick -> {
                _uiState.update { it.copy(showConfirmDialog = true) }
            }

            is StudentOfferDetailEvent.OnPresentationCardChanged -> {
                _uiState.update {
                    it.copy(
                        presentationCardInput = it.presentationCardInput.copy(text = event.text)
                    )
                }
            }

            StudentOfferDetailEvent.OnDismissDialog -> {
                _uiState.update { it.copy(showConfirmDialog = false) }
            }

            StudentOfferDetailEvent.OnConfirmRegister -> {
                val offerId = _uiState.value.currentOfferId ?: return
                _uiState.update { it.copy(showConfirmDialog = false, isRegistering = true) }
                registerToOffer(offerId)
            }

            StudentOfferDetailEvent.OnNavigateBack -> {
            }
        }
    }

    private fun loadOffer(offerId: Int) {
        launchWithErrorWrapper(onError = {}) {
            val offer = offerRepository.getOfferById(offerId)
            val presentationCard = offer.presentationCard.orEmpty()
            val modalityType = OfferType.from(offer.type)
            val modalityRes = when (modalityType) {
                OfferType.REGULAR -> R.string.offer_type_regular_text
                OfferType.ADULTS -> R.string.offer_type_adults_text
            }

            _uiState.update {
                it.copy(
                    isLoaded = true,
                    companyPhoto = offer.companyPhoto,
                    titleInput = TextFieldUiModel(offer.title),
                    companyInput = TextFieldUiModel(offer.company),
                    degreeInput = TextFieldUiModel(offer.degreeName),
                    startDate = offer.startDate,
                    endDate = offer.endDate,
                    modalityResId = modalityRes,
                    descriptionInput = TextFieldUiModel(offer.description.orEmpty()),
                    vacanciesInput = TextFieldUiModel(offer.vacancies.toString()),
                    viewsInput = TextFieldUiModel(offer.views.toString()),
                    contactNameInput = TextFieldUiModel(offer.contactName),
                    contactEmailInput = TextFieldUiModel(offer.contactEmail),
                    contactPhoneInput = TextFieldUiModel(offer.contactPhone.orEmpty()),
                    addressInput = TextFieldUiModel(offer.address),
                    postalCodeInput = TextFieldUiModel(offer.postalCode),
                    currentOfferId = offer.id,
                    isAlreadyRegistered = offer.inscribe,
                    presentationCardInput = TextFieldUiModel(presentationCard)
                )
            }
        }
    }

    private fun registerToOffer(offerId: Int) {
        launchWithErrorWrapper(
            onError = {
                _uiState.update { it.copy(isRegistering = false, isAlreadyRegistered = true) }
            }
        ) {
            offerRepository.registerCandidacy(
                presentationCard = _uiState.value.presentationCardInput.text,
                offerId = offerId
            )

            _uiState.update {
                it.copy(isRegistering = false, isAlreadyRegistered = true)
            }
        }
    }

    data class StudentOfferDetailUiState(
        val isLoaded: Boolean = false,
        val companyPhoto: String? = null,
        val titleInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val companyInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val degreeInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val startDate: LocalDate? = null,
        val endDate: LocalDate? = null,
        val modalityResId: Int = R.string.offer_type_regular_text,
        val descriptionInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val vacanciesInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val viewsInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val contactNameInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val contactEmailInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val contactPhoneInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val addressInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val postalCodeInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val presentationCardInput: TextFieldUiModel<String> = TextFieldUiModel(""),
        val isAlreadyRegistered: Boolean? = null,
        val currentOfferId: Int? = null,
        val isRegistering: Boolean = false,
        val showConfirmDialog: Boolean = false
    )

    sealed interface StudentOfferDetailEvent {
        data class OnLoadOffer(val offerId: Int) : StudentOfferDetailEvent
        data class OnPresentationCardChanged(val text: String) : StudentOfferDetailEvent
        object OnRegisterClick : StudentOfferDetailEvent
        object OnDismissDialog : StudentOfferDetailEvent
        object OnConfirmRegister : StudentOfferDetailEvent
        object OnNavigateBack : StudentOfferDetailEvent
    }
}
