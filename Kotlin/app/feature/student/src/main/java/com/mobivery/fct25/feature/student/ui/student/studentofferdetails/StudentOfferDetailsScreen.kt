package com.mobivery.fct25.feature.student.ui.student.studentofferdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithLeftCancel
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXXS
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.StudentOfferDetailViewModel.StudentOfferDetailEvent
import java.time.format.DateTimeFormatter

@Composable
fun StudentOfferDetailsScreen(
    offerId: Int,
    navController: NavController,
    onNavigateBack: () -> Unit,
    viewModel: StudentOfferDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.studentOfferDetailUiState.collectAsStateWithLifecycle()

    val alreadyLoaded = remember(offerId) { mutableStateOf(false) }

    LaunchedEffect(offerId) {
        if (!alreadyLoaded.value) {
            viewModel.handle(StudentOfferDetailEvent.OnLoadOffer(offerId))
            alreadyLoaded.value = true
        }
    }

    BackHandler {
        viewModel.handle(StudentOfferDetailEvent.OnNavigateBack)

        if (uiState.isAlreadyRegistered == true && uiState.currentOfferId != null) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("offer_registered_id", uiState.currentOfferId)
        }

        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopBarWithLeftCancel(
                title = stringResource(id = R.string.offer_detail_screen_title_text),
                onCancelClick = {
                    viewModel.handle(StudentOfferDetailEvent.OnNavigateBack)

                    if (uiState.isAlreadyRegistered == true && uiState.currentOfferId != null) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("offer_registered_id", uiState.currentOfferId)
                    }

                    onNavigateBack()
                }
            )
        },
        containerColor = AppColors.surface
    ) { innerPadding ->
        if (!uiState.isLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Loader(viewModel)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val painter = getCompanyPainter(uiState.companyPhoto)

                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.offer_list_icon_description_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(start = SPACING_XS, end = SPACING_XS)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.FillWidth
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .imePadding()
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = SPACING_XXXS,
                        shadowElevation = SPACING_XXXS,
                        color = AppColors.surface,
                        modifier = Modifier
                            .padding(horizontal = SPACING_M)
                    ) {
                        Column(
                            modifier = Modifier.padding(SPACING_M)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = uiState.titleInput.text,
                                    style = AppTypography.titleLarge,
                                    color = AppColors.onSurface,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_M))

                            Text(
                                text = uiState.companyInput.text,
                                style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AppColors.onSurfaceSecondary
                            )

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_formation_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.degreeInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_date_offer_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                val start = uiState.startDate?.format(dateFormatter)
                                val end = uiState.endDate?.format(dateFormatter)

                                Text(
                                    text = if (start != null && end != null) "$start - $end" else "",
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                ->
                                Text(
                                    text = stringResource(id = R.string.offer_list_modality_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = stringResource(id = uiState.modalityResId),
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_XS))

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = AppColors.onSurface.copy(alpha = 0.1f)
                            )

                            Spacer(modifier = Modifier.height(SPACING_XS))

                            if (uiState.descriptionInput.text.isNotBlank()) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = stringResource(id = R.string.offer_description_text),
                                        style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = AppColors.onSurfaceSecondary
                                    )
                                    Spacer(modifier = Modifier.height(SPACING_XXS))
                                    Text(
                                        text = uiState.descriptionInput.text,
                                        style = AppTypography.bodySmall,
                                        color = AppColors.onSurface,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(SPACING_XS))
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = AppColors.onSurface.copy(alpha = 0.1f)
                                    )
                                    Spacer(modifier = Modifier.height(SPACING_XS))
                                }
                            }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_vacancies_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.vacanciesInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_watchers_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.viewsInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_XS))

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = AppColors.onSurface.copy(alpha = 0.1f)
                            )

                            Spacer(modifier = Modifier.height(SPACING_XS))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_contact_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.contactNameInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_mail_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.contactEmailInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            if (uiState.contactPhoneInput.text.isNotBlank()) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = stringResource(id = R.string.offer_list_phone_text),
                                        style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = AppColors.onSurfaceSecondary
                                    )
                                    Spacer(modifier = Modifier.height(SPACING_XXS))
                                    Text(
                                        text = uiState.contactPhoneInput.text,
                                        style = AppTypography.bodySmall,
                                        color = AppColors.onSurfaceSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                            }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_address_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.addressInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(SPACING_S))

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.offer_list_postal_code_text),
                                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = AppColors.onSurfaceSecondary
                                )
                                Spacer(modifier = Modifier.height(SPACING_XXS))
                                Text(
                                    text = uiState.postalCodeInput.text,
                                    style = AppTypography.bodySmall,
                                    color = AppColors.onSurfaceSecondary
                                )
                            }
                        }
                    }

                    if (uiState.isAlreadyRegistered != true) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SPACING_M, vertical = SPACING_S),
                            shape = RoundedCornerShape(SPACING_S),
                            tonalElevation = 1.dp,
                            shadowElevation = 1.dp,
                            color = AppColors.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(SPACING_M)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.candidacy_presentation_card_text),
                                    style = AppTypography.titleSmall,
                                    color = AppColors.onSurface
                                )

                                Spacer(modifier = Modifier.height(SPACING_S))

                                CustomTextField(
                                    value = uiState.presentationCardInput.text,
                                    onValueChange = {
                                        viewModel.handle(
                                            StudentOfferDetailEvent.OnPresentationCardChanged(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = stringResource(id = R.string.candidacy_type_presentation_card_text),
                                    singleLine = false,
                                    hideIndicator = true,
                                    textFieldModifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(SPACING_M))

                    BottomBarContent(
                        isAlreadyRegistered = uiState.isAlreadyRegistered == true,
                        isRegistering = uiState.isRegistering,
                        offerId = uiState.currentOfferId,
                        onRegisterClick = {
                            viewModel.handle(StudentOfferDetailEvent.OnRegisterClick)
                        }
                    )
                }
            }
        }
        if (uiState.showConfirmDialog) {
            InfoDialog(
                title = stringResource(id = R.string.candidacy_confirm_register_text),
                message = stringResource(id = R.string.candidacy_register_question_text),
                primaryButton = stringResource(id = R.string.candidacy_register_text),
                secondaryButton = stringResource(id = R.string.candidacy_dialog_cancel_button),
                onPrimaryButtonClick = { viewModel.handle(StudentOfferDetailEvent.OnConfirmRegister) },
                onSecondaryButtonClick = { viewModel.handle(StudentOfferDetailEvent.OnDismissDialog) },
                onDismiss = { viewModel.handle(StudentOfferDetailEvent.OnDismissDialog) },
                primaryButtonColor = AppColors.primary,
                secondaryButtonColor = AppColors.onSurface
            )
        }
    }
}

@Composable
private fun BottomBarContent(
    isAlreadyRegistered: Boolean,
    isRegistering: Boolean,
    offerId: Int?,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SPACING_M),
        contentAlignment = Alignment.Center
    ) {
        when {
            isAlreadyRegistered && !isRegistering -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_verified),
                        contentDescription = stringResource(id = R.string.offer_list_icon_inscribe_text),
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = SPACING_XXS)
                    )
                    Text(
                        text = stringResource(id = R.string.offer_list_inscribe_text),
                        style = AppTypography.titleMedium,
                        color = AppColors.success
                    )
                }
            }

            else -> {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (isRegistering)
                        stringResource(id = R.string.common_loading_text)
                    else
                        stringResource(id = R.string.offer_list_register_text),
                    enabled = !isRegistering && offerId != null,
                    onClick = onRegisterClick
                )
            }
        }
    }
}

@Composable
fun getCompanyPainter(photo: String?): Painter =
    if (photo.isNullOrBlank()) painterResource(id = R.drawable.practicapp)
    else rememberAsyncImagePainter(model = photo)
