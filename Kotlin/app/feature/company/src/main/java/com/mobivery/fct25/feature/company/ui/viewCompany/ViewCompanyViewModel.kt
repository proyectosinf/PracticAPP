package com.mobivery.fct25.feature.company.ui.viewCompany

import android.net.Uri
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModel
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.model.TextFieldUiModel
import com.mobivery.fct25.domain.repository.AuthRepository
import com.mobivery.fct25.feature.company.model.CompanyFormError
import com.mobivery.fct25.feature.company.ui.viewCompany.ViewCompanyViewModel.ViewCompanyEvent
import com.mobivery.fct25.feature.company.ui.viewCompany.ViewCompanyViewModel.ViewCompanyUiState
import com.mobivery.template.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ViewCompanyViewModelInterface : BaseViewModelInterface {
    val viewCompanyUiState: StateFlow<ViewCompanyUiState>
    fun handle(event: ViewCompanyEvent)
}

@HiltViewModel
class ViewCompanyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val companyRepository: CompanyRepository
) : BaseViewModel(), ViewCompanyViewModelInterface {

    companion object {
        private val DEFAULT_LOGO_URL = R.drawable.practicapp
    }

    private val _viewCompanyUiState = MutableStateFlow(ViewCompanyUiState())
    override val viewCompanyUiState: StateFlow<ViewCompanyUiState> =
        _viewCompanyUiState.asStateFlow()

    init {
        loadCompany()
    }

    override fun handle(event: ViewCompanyEvent) {
        when (event) {
            is ViewCompanyEvent.OnLogoSelected -> uploadLogo(event.uri)
            ViewCompanyEvent.OnChangeLogoRequested -> _viewCompanyUiState.update {
                it.copy(shouldOpenImagePicker = true)
            }

            ViewCompanyEvent.OnImagePickerOpened -> _viewCompanyUiState.update {
                it.copy(shouldOpenImagePicker = false)
            }

            ViewCompanyEvent.OnErrorDialogDismissed -> _viewCompanyUiState.update {
                it.copy(showErrorDialog = false, errorMessageRes = null)
            }

            ViewCompanyEvent.RequestLogoutConfirmation -> _viewCompanyUiState.update {
                it.copy(showLogoutConfirmationDialog = true)
            }

            ViewCompanyEvent.CancelLogout -> _viewCompanyUiState.update {
                it.copy(showLogoutConfirmationDialog = false)
            }

            ViewCompanyEvent.ConfirmLogout -> logout()
            ViewCompanyEvent.Logout -> logout()
        }
    }

    private fun loadCompany() {
        launchWithErrorWrapper(
            onError = {
                showError(R.string.company_view_company_load_error_text)
            }
        ) {
            val company = companyRepository.getCurrentUserCompany()

            _viewCompanyUiState.update {
                it.copy(
                    logoInput = TextFieldUiModel((company.logo ?: DEFAULT_LOGO_URL).toString()),
                    nameInput = TextFieldUiModel(company.name),
                    cifInput = TextFieldUiModel(company.cif),
                    sectorInput = TextFieldUiModel(company.sector),
                    webInput = TextFieldUiModel(company.web.orEmpty())
                )
            }
        }
    }

    private fun uploadLogo(uri: Uri) {
        launchWithErrorWrapper(
            onError = {
                showError(R.string.company_logo_upload_error_text)
            }
        ) {
            companyRepository.uploadCompanyImage(uri)
            loadCompany()
        }
    }

    private fun logout() {
        _viewCompanyUiState.update { it.copy(showLogoutConfirmationDialog = false) }
        launchWithErrorWrapper(showLoading = false) {
            authRepository.logout()
        }
    }

    private fun showError(messageResId: Int) {
        _viewCompanyUiState.update {
            it.copy(showErrorDialog = true, errorMessageRes = messageResId)
        }
    }

    data class ViewCompanyUiState(
        val logoInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val nameInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val cifInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val sectorInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val webInput: TextFieldUiModel<CompanyFormError> = TextFieldUiModel(""),
        val showErrorDialog: Boolean = false,
        val errorMessageRes: Int? = null,
        val showLogoutConfirmationDialog: Boolean = false,
        val shouldOpenImagePicker: Boolean = false
    )

    sealed interface ViewCompanyEvent {
        data class OnLogoSelected(val uri: Uri) : ViewCompanyEvent
        data object OnChangeLogoRequested : ViewCompanyEvent
        data object OnImagePickerOpened : ViewCompanyEvent
        data object Logout : ViewCompanyEvent
        data object ConfirmLogout : ViewCompanyEvent
        data object CancelLogout : ViewCompanyEvent
        data object RequestLogoutConfirmation : ViewCompanyEvent
        data object OnErrorDialogDismissed : ViewCompanyEvent
    }
}
