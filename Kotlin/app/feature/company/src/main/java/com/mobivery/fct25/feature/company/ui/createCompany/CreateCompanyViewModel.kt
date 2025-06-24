package com.mobivery.fct25.feature.company.ui.createcompany

import android.content.Context
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.feature.company.model.CompanyFormError
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyViewModel.CreateCompanyUiState
import com.mobivery.template.domain.model.company.CompanyModel
import com.mobivery.template.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface CreateCompanyViewModelInterface : BaseViewModelInterface {
    val createCompanyUiState: StateFlow<CreateCompanyUiState>
    fun handle(event: CreateCompanyEvent)
}

@HiltViewModel
class CreateCompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel(), CreateCompanyViewModelInterface {

    private val _createCompanyUiState = MutableStateFlow(CreateCompanyUiState())
    override val createCompanyUiState = _createCompanyUiState.asStateFlow()

    private companion object {
        val CIF_REGEX = Regex("^[A-Z][0-9]{8}\$|^[0-9]{8}[A-Z]\$")
        private const val HTTP_PREFIX = "http://"
        private const val HTTPS_PREFIX = "https://"
    }

    override fun handle(event: CreateCompanyEvent) {
        when (event) {
            is CreateCompanyEvent.OnNameChanged -> updateName(event.name)
            is CreateCompanyEvent.OnCifChanged -> updateCif(event.cif)
            is CreateCompanyEvent.OnSectorChanged -> updateSector(event.sector)
            is CreateCompanyEvent.OnWebChanged -> updateWeb(event.web)
            CreateCompanyEvent.NameLostFocus -> validateName()
            CreateCompanyEvent.CifLostFocus -> validateCif()
            CreateCompanyEvent.SectorLostFocus -> validateSector()
            CreateCompanyEvent.WebLostFocus -> validateWeb()
            CreateCompanyEvent.Submit -> validateFormAndSubmit()
            CreateCompanyEvent.CloseErrorDialog -> closeDialogs()
            CreateCompanyEvent.Logout -> logout()
            CreateCompanyEvent.OnLogoutButtonClick -> showLogoutDialog()
            CreateCompanyEvent.CancelLogout -> hideLogoutDialog()
            CreateCompanyEvent.ConfirmLogout -> logout()
            CreateCompanyEvent.DidNavigate -> clearNavigation()
        }
    }

    private fun updateName(name: String) {
        _createCompanyUiState.update {
            it.copy(nameInput = TextFieldUiModel(name, null))
        }
    }

    private fun updateCif(cif: String) {
        _createCompanyUiState.update {
            it.copy(cifInput = TextFieldUiModel(cif, null))
        }
    }

    private fun updateSector(sector: String) {
        _createCompanyUiState.update {
            it.copy(sectorInput = TextFieldUiModel(sector, null))
        }
    }

    private fun updateWeb(web: String) {
        _createCompanyUiState.update {
            it.copy(webInput = TextFieldUiModel(web, null))
        }
    }

    private fun validateName(): Boolean {
        val error =
            if (_createCompanyUiState.value.nameInput.text.isBlank()) CompanyFormError.EMPTY else null
        _createCompanyUiState.update {
            it.copy(nameInput = it.nameInput.copy(errorType = error))
        }
        return error == null
    }

    private fun validateCif(): Boolean {
        val text = _createCompanyUiState.value.cifInput.text
        val error = when {
            text.isBlank() -> CompanyFormError.EMPTY
            !CIF_REGEX.matches(text) -> CompanyFormError.INVALID_CIF
            else -> null
        }
        _createCompanyUiState.update {
            it.copy(cifInput = it.cifInput.copy(errorType = error))
        }
        return error == null
    }

    private fun validateSector(): Boolean {
        val error =
            if (_createCompanyUiState.value.sectorInput.text.isBlank()) CompanyFormError.EMPTY else null
        _createCompanyUiState.update {
            it.copy(sectorInput = it.sectorInput.copy(errorType = error))
        }
        return error == null
    }

    private fun validateWeb(): Boolean {
        val text = _createCompanyUiState.value.webInput.text
        val isValidFormat = android.util.Patterns.WEB_URL.matcher(text).matches()
        val startsWithHttp = text.startsWith(HTTP_PREFIX, ignoreCase = true) || text.startsWith(
            HTTPS_PREFIX,
            ignoreCase = true
        )

        val error = if (text.isNotBlank() && (!isValidFormat || !startsWithHttp)) {
            CompanyFormError.INVALID_WEB
        } else null

        _createCompanyUiState.update {
            it.copy(webInput = it.webInput.copy(errorType = error))
        }
        return error == null
    }

    private fun validateFormAndSubmit() {
        val isValid = listOf(
            validateName(),
            validateCif(),
            validateSector(),
            validateWeb()
        ).all { it }

        if (isValid) {
            submitCompany()
        }
    }

    private fun submitCompany() {
        val state = _createCompanyUiState.value

        val company = CompanyModel(
            name = state.nameInput.text,
            cif = state.cifInput.text,
            sector = state.sectorInput.text,
            web = state.webInput.text.ifBlank { null }
        )

        launchWithErrorWrapper(
            showDefaultError = false,
            onError = {
                _createCompanyUiState.update {
                    it.copy(
                        showErrorDialog = true,
                        errorDialogTitle = context.getString(R.string.company_register_failure_title_text),
                        errorDialogMessage = context.getString(R.string.company_register_error_name_or_cif_exists_text)
                    )
                }
            }
        ) {
            companyRepository.createCompany(company)
            _createCompanyUiState.update {
                it.copy(navigation = CreateCompanyNavigation.NavigateToCompany)
            }
        }
    }

    private fun showLogoutDialog() {
        _createCompanyUiState.update { it.copy(showLogoutConfirmationDialog = true) }
    }

    private fun hideLogoutDialog() {
        _createCompanyUiState.update { it.copy(showLogoutConfirmationDialog = false) }
    }

    private fun logout() {
        hideLogoutDialog()
        launchWithErrorWrapper {
            authRepository.logout()
        }
    }

    private fun closeDialogs() {
        _createCompanyUiState.update {
            it.copy(
                showErrorDialog = false,
                errorDialogTitle = null,
                errorDialogMessage = null
            )
        }
    }

    private fun clearNavigation() {
        _createCompanyUiState.update {
            it.copy(navigation = null)
        }
    }

    data class CreateCompanyUiState(
        val nameInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val cifInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val sectorInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val webInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val showLogoutConfirmationDialog: Boolean = false,
        val showErrorDialog: Boolean = false,
        val errorDialogTitle: String? = null,
        val errorDialogMessage: String? = null,
        val navigation: CreateCompanyNavigation? = null
    )

    sealed interface CreateCompanyNavigation {
        data object NavigateToCompany : CreateCompanyNavigation
    }
}

sealed interface CreateCompanyEvent {
    data class OnNameChanged(val name: String) : CreateCompanyEvent
    data class OnCifChanged(val cif: String) : CreateCompanyEvent
    data class OnSectorChanged(val sector: String) : CreateCompanyEvent
    data class OnWebChanged(val web: String) : CreateCompanyEvent
    data object NameLostFocus : CreateCompanyEvent
    data object CifLostFocus : CreateCompanyEvent
    data object SectorLostFocus : CreateCompanyEvent
    data object WebLostFocus : CreateCompanyEvent
    data object Submit : CreateCompanyEvent
    data object CloseErrorDialog : CreateCompanyEvent
    data object Logout : CreateCompanyEvent
    data object OnLogoutButtonClick : CreateCompanyEvent
    data object ConfirmLogout : CreateCompanyEvent
    data object CancelLogout : CreateCompanyEvent
    data object DidNavigate : CreateCompanyEvent
}
