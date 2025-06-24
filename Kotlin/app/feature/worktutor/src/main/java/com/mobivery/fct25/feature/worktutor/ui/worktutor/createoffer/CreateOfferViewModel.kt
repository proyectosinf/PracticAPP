package com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer

import Offer
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.extension.toMillis
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.feature.worktutor.model.OfferFormError
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.CreateOfferViewModel.CreateOfferEvent
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.CreateOfferViewModel.CreateOfferUiState
import com.mobivery.template.domain.model.degree.Degree
import com.mobivery.template.domain.model.offer.OfferType
import com.mobivery.template.domain.repository.OfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

interface CreateOfferViewModelInterface : BaseViewModelInterface {
    val createOfferUiState: StateFlow<CreateOfferUiState>
    fun handle(event: CreateOfferEvent)
}

@HiltViewModel
class CreateOfferViewModel @Inject constructor(
    private val offerRepository: OfferRepository
) : BaseViewModel(), CreateOfferViewModelInterface {

    private val _createOfferUiState = MutableStateFlow(CreateOfferUiState())
    override val createOfferUiState = _createOfferUiState.asStateFlow()

    private companion object {
        const val DEFAULT_VIEWS = 0
        const val EMPTY_COMPANY_NAME = ""
        const val EMPTY_COMPANY_PHOTO = ""
    }

    init {
        loadDegrees()
    }

    override fun handle(event: CreateOfferEvent) {
        when (event) {
            is CreateOfferEvent.OnTitleChanged,
            is CreateOfferEvent.OnDescriptionChanged,
            is CreateOfferEvent.OnVacanciesChanged,
            is CreateOfferEvent.OnStartDateSelected,
            is CreateOfferEvent.OnEndDateSelected,
            is CreateOfferEvent.OnTypeChanged,
            is CreateOfferEvent.OnDegreeChanged,
            is CreateOfferEvent.OnAddressChanged,
            is CreateOfferEvent.OnPostalCodeChanged,
            is CreateOfferEvent.OnContactNameChanged,
            is CreateOfferEvent.OnContactEmailChanged,
            is CreateOfferEvent.OnContactPhoneChanged -> handleFieldChange(event)

            CreateOfferEvent.OnTitleLostFocus,
            CreateOfferEvent.OnVacanciesLostFocus,
            CreateOfferEvent.OnStartDateLostFocus,
            CreateOfferEvent.OnEndDateLostFocus,
            CreateOfferEvent.OnTypeLostFocus,
            CreateOfferEvent.OnDegreeLostFocus,
            CreateOfferEvent.OnAddressLostFocus,
            CreateOfferEvent.OnPostalCodeLostFocus,
            CreateOfferEvent.OnContactNameLostFocus,
            CreateOfferEvent.OnContactEmailLostFocus,
            CreateOfferEvent.OnContactPhoneLostFocus -> handleFocusLost(event)

            CreateOfferEvent.OnStartDateDialogRequest,
            CreateOfferEvent.OnStartDateDialogDismissed,
            CreateOfferEvent.OnEndDateDialogRequest,
            CreateOfferEvent.OnEndDateDialogDismissed -> handleDialogEvent(event)

            CreateOfferEvent.OnSubmitClick,
            CreateOfferEvent.DidNavigate -> handleActionEvent(event)
        }
    }

    private fun handleFieldChange(event: CreateOfferEvent) {
        when (event) {
            is CreateOfferEvent.OnTitleChanged -> updateField(
                event.title,
                OfferFormError.EMPTY_TITLE
            ) { copy(titleInput = TextFieldUiModel(event.title, it)) }

            is CreateOfferEvent.OnDescriptionChanged -> _createOfferUiState.update {
                it.copy(descriptionInput = it.descriptionInput.copy(text = event.description))
            }

            is CreateOfferEvent.OnVacanciesChanged -> {
                val value = event.vacancies
                val error = when {
                    value.isBlank() -> OfferFormError.EMPTY_VACANCIES
                    value.toIntOrNull() == null || value.toInt() <= 0 -> OfferFormError.INVALID_VACANCIES
                    else -> null
                }
                _createOfferUiState.update {
                    it.copy(vacanciesInput = TextFieldUiModel(value, error))
                }
            }

            is CreateOfferEvent.OnStartDateSelected -> {
                val date = LocalDate.of(event.year, event.month + 1, event.day)
                _createOfferUiState.update { it.copy(startDate = date) }
            }

            is CreateOfferEvent.OnEndDateSelected -> {
                val date = LocalDate.of(event.year, event.month + 1, event.day)
                _createOfferUiState.update { it.copy(endDate = date) }
            }

            is CreateOfferEvent.OnTypeChanged -> _createOfferUiState.update {
                it.copy(type = event.type, typeError = null)
            }

            is CreateOfferEvent.OnDegreeChanged -> {
                val matchedDegree = _createOfferUiState.value.degreeOptions
                    .firstOrNull { it.name == event.degree }

                updateField(event.degree, OfferFormError.EMPTY_DEGREE) {
                    copy(
                        degreeInput = TextFieldUiModel(event.degree, it),
                        selectedDegreeId = matchedDegree?.id
                    )
                }
            }

            is CreateOfferEvent.OnAddressChanged -> updateField(
                event.address,
                OfferFormError.EMPTY_ADDRESS
            ) { copy(addressInput = TextFieldUiModel(event.address, it)) }

            is CreateOfferEvent.OnPostalCodeChanged -> _createOfferUiState.update {
                it.copy(postalCodeInput = it.postalCodeInput.copy(text = event.postalCode))
            }

            is CreateOfferEvent.OnContactNameChanged -> updateField(
                event.contactName,
                OfferFormError.EMPTY_CONTACT_NAME
            ) { copy(contactNameInput = TextFieldUiModel(event.contactName, it)) }

            is CreateOfferEvent.OnContactEmailChanged -> _createOfferUiState.update {
                it.copy(contactEmailInput = it.contactEmailInput.copy(text = event.contactEmail))
            }

            is CreateOfferEvent.OnContactPhoneChanged -> _createOfferUiState.update {
                it.copy(contactPhoneInput = it.contactPhoneInput.copy(text = event.contactPhone))
            }

            else -> Unit
        }
    }

    private fun handleFocusLost(event: CreateOfferEvent) {
        when (event) {
            CreateOfferEvent.OnTitleLostFocus -> validateNotEmpty(
                _createOfferUiState.value.titleInput,
                OfferFormError.EMPTY_TITLE
            ) { copy(titleInput = it) }

            CreateOfferEvent.OnVacanciesLostFocus -> validateVacancies()
            CreateOfferEvent.OnStartDateLostFocus -> validateStartDate()
            CreateOfferEvent.OnEndDateLostFocus -> validateEndDate()
            CreateOfferEvent.OnTypeLostFocus -> validateType()

            CreateOfferEvent.OnDegreeLostFocus -> validateNotEmpty(
                _createOfferUiState.value.degreeInput,
                OfferFormError.EMPTY_DEGREE
            ) { copy(degreeInput = it) }

            CreateOfferEvent.OnAddressLostFocus -> validateNotEmpty(
                _createOfferUiState.value.addressInput,
                OfferFormError.EMPTY_ADDRESS
            ) { copy(addressInput = it) }

            CreateOfferEvent.OnPostalCodeLostFocus -> validatePostalCode()

            CreateOfferEvent.OnContactNameLostFocus -> validateNotEmpty(
                _createOfferUiState.value.contactNameInput,
                OfferFormError.EMPTY_CONTACT_NAME
            ) { copy(contactNameInput = it) }

            CreateOfferEvent.OnContactEmailLostFocus -> validateContactEmail()
            CreateOfferEvent.OnContactPhoneLostFocus -> validateContactPhoneAndUpdate()

            else -> Unit
        }
    }

    private fun handleDialogEvent(event: CreateOfferEvent) {
        when (event) {
            CreateOfferEvent.OnStartDateDialogRequest -> _createOfferUiState.update {
                it.copy(showStartDateDialog = true)
            }

            CreateOfferEvent.OnStartDateDialogDismissed -> _createOfferUiState.update {
                it.copy(showStartDateDialog = false)
            }

            CreateOfferEvent.OnEndDateDialogRequest -> _createOfferUiState.update {
                it.copy(showEndDateDialog = true, endDateTouched = true)
            }

            CreateOfferEvent.OnEndDateDialogDismissed -> _createOfferUiState.update {
                it.copy(showEndDateDialog = false)
            }

            else -> Unit
        }
    }

    private fun handleActionEvent(event: CreateOfferEvent) {
        when (event) {
            CreateOfferEvent.OnSubmitClick -> onSubmit()
            CreateOfferEvent.DidNavigate -> _createOfferUiState.update {
                it.copy(navigation = null)
            }

            else -> Unit
        }
    }

    private fun loadDegrees() {
        launchWithErrorWrapper {
            val degrees = offerRepository.getDegrees()
            _createOfferUiState.update {
                it.copy(degreeOptions = degrees)
            }
        }
    }

    private fun validateNotEmpty(
        input: TextFieldUiModel<OfferFormError>,
        error: OfferFormError,
        update: CreateOfferUiState.(TextFieldUiModel<OfferFormError>) -> CreateOfferUiState
    ) {
        val newInput = input.copy(errorType = if (input.text.isBlank()) error else null)
        _createOfferUiState.update {
            it.update(newInput)
        }
    }

    private fun validateVacancies() {
        val text = _createOfferUiState.value.vacanciesInput.text
        val error = when {
            text.isBlank() -> OfferFormError.EMPTY_VACANCIES
            text.toIntOrNull() == null || text.toInt() <= 0 -> OfferFormError.INVALID_VACANCIES
            else -> null
        }
        _createOfferUiState.update {
            it.copy(vacanciesInput = it.vacanciesInput.copy(errorType = error))
        }
    }

    private fun validateStartDate() {
        val startDate = _createOfferUiState.value.startDate
        val endDate = _createOfferUiState.value.endDate
        val now = LocalDate.now()

        val error = when {
            startDate == null -> OfferFormError.START_DATE_REQUIRED
            !startDate.isAfter(now) -> OfferFormError.INVALID_DATES
            endDate != null && !startDate.isBefore(endDate) -> OfferFormError.START_DATE_AFTER_END_DATE
            else -> null
        }

        _createOfferUiState.update {
            it.copy(startDate = startDate, startDateError = error)
        }
    }

    private fun validateEndDate() {
        val today = LocalDate.now()
        val startDate = _createOfferUiState.value.startDate
        val endDate = _createOfferUiState.value.endDate

        val minEndDate = today.plusDays(2)

        val error = when {
            endDate == null -> OfferFormError.END_DATE_REQUIRED
            endDate.isBefore(minEndDate) -> OfferFormError.INVALID_DATES
            startDate != null && !endDate.isAfter(startDate) -> OfferFormError.START_DATE_AFTER_END_DATE
            else -> null
        }

        _createOfferUiState.update {
            it.copy(endDateError = error)
        }
    }

    private fun validateType() {
        val error =
            if (_createOfferUiState.value.type == null) OfferFormError.TYPE_REQUIRED else null
        _createOfferUiState.update { it.copy(typeError = error) }
    }

    private fun validatePostalCode() {
        val code = _createOfferUiState.value.postalCodeInput.text
        val error = when {
            code.isBlank() -> OfferFormError.EMPTY_POSTAL_CODE
            code.length != 5 || !code.all { it.isDigit() } -> OfferFormError.INVALID_POSTAL_CODE
            else -> null
        }
        _createOfferUiState.update {
            it.copy(postalCodeInput = it.postalCodeInput.copy(errorType = error))
        }
    }

    private fun validateContactEmail() {
        val email = _createOfferUiState.value.contactEmailInput.text
        val error = when {
            email.isBlank() -> OfferFormError.EMPTY_CONTACT_EMAIL
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> OfferFormError.INVALID_CONTACT_EMAIL

            else -> null
        }
        _createOfferUiState.update {
            it.copy(contactEmailInput = it.contactEmailInput.copy(errorType = error))
        }
    }

    private fun validateContactPhoneAndUpdate() {
        val phone = _createOfferUiState.value.contactPhoneInput.text
        val error = when {
            phone.isBlank() -> null
            !phone.matches(Regex("^\\d{9}$")) -> OfferFormError.INVALID_CONTACT_PHONE
            else -> null
        }

        _createOfferUiState.update {
            it.copy(contactPhoneInput = it.contactPhoneInput.copy(errorType = error))
        }
    }

    private fun onSubmit() {
        validateAllFields()
        if (!hasErrors()) {
            submitOffer()
        }
    }

    private fun validateAllFields() {
        handle(CreateOfferEvent.OnTitleLostFocus)
        handle(CreateOfferEvent.OnVacanciesLostFocus)
        handle(CreateOfferEvent.OnTypeLostFocus)
        handle(CreateOfferEvent.OnDegreeLostFocus)
        handle(CreateOfferEvent.OnAddressLostFocus)
        handle(CreateOfferEvent.OnPostalCodeLostFocus)
        handle(CreateOfferEvent.OnContactNameLostFocus)
        handle(CreateOfferEvent.OnContactEmailLostFocus)
        handle(CreateOfferEvent.OnStartDateLostFocus)
        handle(CreateOfferEvent.OnEndDateLostFocus)
        handle(CreateOfferEvent.OnContactPhoneLostFocus)
    }

    private fun hasErrors(): Boolean {
        val state = _createOfferUiState.value
        return listOfNotNull(
            state.titleInput.errorType,
            state.vacanciesInput.errorType,
            state.degreeInput.errorType,
            state.addressInput.errorType,
            state.postalCodeInput.errorType,
            state.contactNameInput.errorType,
            state.contactEmailInput.errorType,
            state.startDateError,
            state.endDateError,
            state.typeError,
            state.contactPhoneInput.errorType
        ).isNotEmpty()
    }

    private fun submitOffer() {
        val state = _createOfferUiState.value
        val offer = Offer(
            id = 0,
            title = state.titleInput.text,
            description = state.descriptionInput.text.ifBlank { null },
            vacancies = state.vacanciesInput.text.toInt(),
            startDate = state.startDate ?: return,
            endDate = state.endDate,
            type = state.type?.value ?: return,
            degreeId = state.selectedDegreeId ?: return,
            degreeName = state.degreeInput.text,
            address = state.addressInput.text,
            postalCode = state.postalCodeInput.text,
            contactName = state.contactNameInput.text,
            contactEmail = state.contactEmailInput.text,
            contactPhone = state.contactPhoneInput.text,
            views = DEFAULT_VIEWS,
            company = EMPTY_COMPANY_NAME,
            companyPhoto = EMPTY_COMPANY_PHOTO,
            inscribe = null,
            inscriptionsCandidacy = 0
        )

        launchWithErrorWrapper {
            offerRepository.createOffer(offer)
            _createOfferUiState.update {
                it.copy(navigation = CreateOfferNavigation.ToPublishedOffers)
            }
        }
    }

    private fun updateField(
        value: String,
        error: OfferFormError,
        update: CreateOfferUiState.(OfferFormError?) -> CreateOfferUiState
    ) {
        _createOfferUiState.update {
            it.update(if (value.isBlank()) error else null)
        }
    }

    data class CreateOfferUiState(
        val navigation: CreateOfferNavigation? = null,
        val titleInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val descriptionInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val vacanciesInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val startDate: LocalDate? = null,
        val startDateError: OfferFormError? = null,
        val startDateTouched: Boolean = false,
        val endDateTouched: Boolean = false,
        val showStartDateDialog: Boolean = false,
        val endDate: LocalDate? = null,
        val endDateError: OfferFormError? = null,
        val showEndDateDialog: Boolean = false,
        val type: OfferType? = null,
        val typeError: OfferFormError? = null,
        val degreeInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val selectedDegreeId: String? = null,
        val addressInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val postalCodeInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val contactNameInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val contactEmailInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val contactPhoneInput: TextFieldUiModel<OfferFormError> = TextFieldUiModel(""),
        val degreeOptions: List<Degree> = emptyList()
    ) {
        val minStartDateMillis: Long?
            get() {
                val today = LocalDate.now()
                val minStart = today.plusDays(1)
                val limit = endDate?.minusDays(1)

                val finalMinStart = if (limit != null && limit.isBefore(minStart)) {
                    limit
                } else {
                    minStart
                }

                return finalMinStart.toMillis()
            }

        val maxStartDateMillis: Long?
            get() = endDate?.minusDays(1)?.toMillis()

        val minEndDateMillis: Long?
            get() {
                val today = LocalDate.now()
                val startDate = startDate
                val minEnd = today.plusDays(2)
                return when {
                    startDate != null -> startDate.plusDays(1).toMillis()
                    else -> minEnd.toMillis()
                }
            }

    }

    sealed interface CreateOfferNavigation {
        data object ToPublishedOffers : CreateOfferNavigation
    }

    sealed interface CreateOfferEvent {
        data class OnTitleChanged(val title: String) : CreateOfferEvent
        data class OnDescriptionChanged(val description: String) : CreateOfferEvent
        data class OnVacanciesChanged(val vacancies: String) : CreateOfferEvent
        data class OnStartDateSelected(val year: Int, val month: Int, val day: Int) :
            CreateOfferEvent

        data class OnEndDateSelected(val year: Int, val month: Int, val day: Int) : CreateOfferEvent
        data class OnTypeChanged(val type: OfferType) : CreateOfferEvent
        data class OnDegreeChanged(val degree: String) : CreateOfferEvent
        data class OnAddressChanged(val address: String) : CreateOfferEvent
        data class OnPostalCodeChanged(val postalCode: String) : CreateOfferEvent
        data class OnContactNameChanged(val contactName: String) : CreateOfferEvent
        data class OnContactEmailChanged(val contactEmail: String) : CreateOfferEvent
        data class OnContactPhoneChanged(val contactPhone: String) : CreateOfferEvent
        data object OnTitleLostFocus : CreateOfferEvent
        data object OnVacanciesLostFocus : CreateOfferEvent
        data object OnTypeLostFocus : CreateOfferEvent
        data object OnStartDateDialogRequest : CreateOfferEvent
        data object OnStartDateDialogDismissed : CreateOfferEvent
        data object OnStartDateLostFocus : CreateOfferEvent
        data object OnEndDateDialogRequest : CreateOfferEvent
        data object OnEndDateDialogDismissed : CreateOfferEvent
        data object OnEndDateLostFocus : CreateOfferEvent
        data object OnDegreeLostFocus : CreateOfferEvent
        data object OnAddressLostFocus : CreateOfferEvent
        data object OnPostalCodeLostFocus : CreateOfferEvent
        data object OnContactNameLostFocus : CreateOfferEvent
        data object OnContactEmailLostFocus : CreateOfferEvent
        data object OnContactPhoneLostFocus : CreateOfferEvent
        data object OnSubmitClick : CreateOfferEvent
        data object DidNavigate : CreateOfferEvent
    }
}
