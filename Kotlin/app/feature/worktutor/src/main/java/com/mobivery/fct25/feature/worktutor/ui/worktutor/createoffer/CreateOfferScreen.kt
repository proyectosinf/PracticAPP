package com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.DatePickerDialog
import com.mobivery.fct25.app.common.designsystem.component.dropdown.DropdownSelector
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.MandatoryTextField
import com.mobivery.fct25.app.common.designsystem.component.textinput.TextFieldWithSuggestion
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithLeftCancel
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.CreateOfferViewModel.CreateOfferEvent
import com.mobivery.template.domain.model.offer.OfferType
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOfferScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPublishedOffers: () -> Unit,
    viewModel: CreateOfferViewModelInterface = hiltViewModel<CreateOfferViewModel>()
) {
    val uiState by viewModel.createOfferUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }

    BackHandler { onNavigateBack() }

    LaunchedEffect(uiState.navigation) {
        when (uiState.navigation) {
            CreateOfferViewModel.CreateOfferNavigation.ToPublishedOffers -> {
                onNavigateToPublishedOffers()
            }

            else -> Unit
        }
        viewModel.handle(CreateOfferEvent.DidNavigate)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = Color.White,
        topBar = {
            TopBarWithLeftCancel(
                containerColor = Color.Transparent,
                onCancelClick = onNavigateBack,
                title = stringResource(R.string.offer_create_offer_text)
            )
        }
    ) { paddingValues ->

        val topPadding = paddingValues.calculateTopPadding()

        Box(
            modifier = Modifier
                .padding(top = topPadding)
                .fillMaxSize()
                .imePadding()
        ) {
            Loader(viewModel = viewModel)
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = SPACING_M, vertical = SPACING_S)
            ) {
                Text(
                    text = stringResource(R.string.offer_offer_detaill_text),
                    style = AppTypography.titleMedium,
                    color = AppColors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SPACING_M, bottom = SPACING_S)
                )

                // Title
                MandatoryTextField(
                    value = uiState.titleInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnTitleChanged(it)) },
                    baseLabel = stringResource(R.string.offer_title_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.titleInput.errorType != null,
                    errorMessage = uiState.titleInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnTitleLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Description
                CustomTextField(
                    value = uiState.descriptionInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnDescriptionChanged(it)) },
                    label = stringResource(R.string.offer_description_text),
                    isError = uiState.descriptionInput.errorType != null,
                    errorMessage = uiState.descriptionInput.errorType?.messageResourceId?.let {
                        stringResource(
                            it
                        )
                    } ?: "",
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Vacancies
                MandatoryTextField(
                    value = uiState.vacanciesInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnVacanciesChanged(it)) },
                    baseLabel = stringResource(R.string.offer_vacances_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.vacanciesInput.errorType != null,
                    errorMessage = uiState.vacanciesInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnVacanciesLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Start Date
                DateField(
                    label = stringResource(R.string.offer_start_date_text),
                    selectedDate = uiState.startDate,
                    onDateSelected = { year, month, day ->
                        viewModel.handle(CreateOfferEvent.OnStartDateSelected(year, month, day))
                    },
                    minDateMillis = uiState.minStartDateMillis,
                    maxDateMillis = uiState.maxStartDateMillis,
                    isError = uiState.startDateError != null,
                    errorMessage = uiState.startDateError?.let { stringResource(it.messageResourceId) }
                        ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnStartDateLostFocus) },
                    showDialog = uiState.showStartDateDialog,
                    onShowDialogRequest = { viewModel.handle(CreateOfferEvent.OnStartDateDialogRequest) },
                    onDismissDialog = { viewModel.handle(CreateOfferEvent.OnStartDateDialogDismissed) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // End Date
                DateField(
                    label = stringResource(R.string.offer_end_date_text),
                    selectedDate = uiState.endDate,
                    onDateSelected = { year, month, day ->
                        viewModel.handle(CreateOfferEvent.OnEndDateSelected(year, month, day))
                    },
                    minDateMillis = uiState.minEndDateMillis,
                    maxDateMillis = null,
                    isError = uiState.endDateError != null,
                    errorMessage = uiState.endDateError?.let { stringResource(it.messageResourceId) }
                        ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnEndDateLostFocus) },
                    showDialog = uiState.showEndDateDialog,
                    onShowDialogRequest = { viewModel.handle(CreateOfferEvent.OnEndDateDialogRequest) },
                    onDismissDialog = { viewModel.handle(CreateOfferEvent.OnEndDateDialogDismissed) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Type
                DropdownSelector(
                    label = stringResource(R.string.offer_offer_type_text),
                    options = OfferType.entries.toList(),
                    selectedOption = uiState.type,
                    onOptionSelected = { viewModel.handle(CreateOfferEvent.OnTypeChanged(it)) },
                    labelMapper = {
                        when (it) {
                            OfferType.REGULAR -> stringResource(R.string.offer_type_regular_text)
                            OfferType.ADULTS -> stringResource(R.string.offer_type_adults_text)
                        }
                    },
                    isError = uiState.typeError != null,
                    errorMessage = uiState.typeError?.messageResourceId?.let { stringResource(it) }
                        ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnTypeLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Degree
                TextFieldWithSuggestion(
                    value = uiState.degreeInput.text,
                    label = stringResource(id = R.string.offer_formation_text),
                    options = uiState.degreeOptions.map { it.name },
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnDegreeChanged(it)) },
                    onSuggestionSelected = { viewModel.handle(CreateOfferEvent.OnDegreeChanged(it)) },
                    isError = uiState.degreeInput.errorType != null,
                    errorMessage = uiState.degreeInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnDegreeLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                Text(
                    text = stringResource(R.string.offer_contact_detaill_text),
                    style = AppTypography.titleMedium,
                    color = AppColors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SPACING_M, bottom = SPACING_S)
                )

                // Address
                MandatoryTextField(
                    value = uiState.addressInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnAddressChanged(it)) },
                    baseLabel = stringResource(R.string.offer_addrees_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.addressInput.errorType != null,
                    errorMessage = uiState.addressInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnAddressLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Postal Code
                MandatoryTextField(
                    value = uiState.postalCodeInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnPostalCodeChanged(it)) },
                    baseLabel = stringResource(R.string.offer_postal_code_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.postalCodeInput.errorType != null,
                    errorMessage = uiState.postalCodeInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnPostalCodeLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Contact name
                MandatoryTextField(
                    value = uiState.contactNameInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnContactNameChanged(it)) },
                    baseLabel = stringResource(R.string.offer_contact_name_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.contactNameInput.errorType != null,
                    errorMessage = uiState.contactNameInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnContactNameLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Contact email
                MandatoryTextField(
                    value = uiState.contactEmailInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnContactEmailChanged(it)) },
                    baseLabel = stringResource(R.string.offer_contact_email_text),
                    mandatoryIndicator = stringResource(R.string.common_mandatory_placeholder_text),
                    isError = uiState.contactEmailInput.errorType != null,
                    errorMessage = uiState.contactEmailInput.errorType?.messageResourceId?.let {
                        stringResource(it)
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnContactEmailLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                // Phone
                CustomTextField(
                    value = uiState.contactPhoneInput.text,
                    onValueChange = { viewModel.handle(CreateOfferEvent.OnContactPhoneChanged(it)) },
                    label = stringResource(id = R.string.offer_phone_text),
                    isError = uiState.contactPhoneInput.errorType != null,
                    errorMessage = uiState.contactPhoneInput.errorType?.messageResourceId?.let {
                        stringResource(
                            it
                        )
                    } ?: "",
                    onFocusLost = { viewModel.handle(CreateOfferEvent.OnContactPhoneLostFocus) }
                )

                Spacer(modifier = Modifier.height(SPACING_L))

                // Submit button
                PrimaryButton(
                    onClick = { viewModel.handle(CreateOfferEvent.OnSubmitClick) },
                    text = stringResource(id = R.string.offer_create_offer_text),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.loading.collectAsStateWithLifecycle().value
                )

                Spacer(modifier = Modifier.height(SPACING_M))
            }
        }
    }
}

@Composable
fun DateField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (Int, Int, Int) -> Unit,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    showDialog: Boolean,
    onShowDialogRequest: () -> Unit,
    onDismissDialog: () -> Unit,
    onFocusLost: () -> Unit = { /* Do nothing */ }
) {
    if (showDialog) {
        val date = selectedDate ?: LocalDate.now()
        DatePickerDialog(
            year = date.year,
            month = date.monthValue - 1,
            day = date.dayOfMonth,
            maxDateMillis = maxDateMillis,
            minDateMillis = minDateMillis,
            onDateSelected = { year, month, day ->
                onDateSelected(year, month, day)
                onDismissDialog()
            },
            onDismiss = {
                onDismissDialog()
                onFocusLost()
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface,
            modifier = Modifier.padding(bottom = SPACING_S)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (isError) AppColors.error else AppColors.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onShowDialogRequest()
                }
                .padding(horizontal = SPACING_M, vertical = SPACING_S)
        ) {
            Text(
                text = selectedDate?.toString() ?: stringResource(R.string.offer_select_text),
                style = AppTypography.bodyMedium,
                color = if (isError) AppColors.error else AppColors.onSurface
            )
        }

        if (isError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = AppTypography.labelSmall,
                color = AppColors.error,
                modifier = Modifier.padding(start = SPACING_M)
            )
        }
    }
}

@Preview(name = "CreateOfferScreen", showBackground = true)
@Composable
private fun CreateOfferScreenPreview() {
    AppTheme {
        CreateOfferScreen(
            onNavigateBack = {},
            onNavigateToPublishedOffers = {}
        )
    }
}
