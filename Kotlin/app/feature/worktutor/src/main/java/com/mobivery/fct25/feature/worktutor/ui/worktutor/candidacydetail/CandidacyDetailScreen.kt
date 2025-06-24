package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithActions
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.CandidacyDetailViewModel.CandidacyDetailEvent
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidacyDetailScreen(
    navController: NavController,
    viewModel: CandidacyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (uiState.navigation) {
            CandidacyDetailViewModel.CandidacyDetailNavigation.NavigateBack -> {
                navController.popBackStack()
                viewModel.handle(CandidacyDetailEvent.OnNavigationHandled)
            }

            null -> Unit
        }
    }

    BackHandler {
        viewModel.handle(CandidacyDetailEvent.OnBackClick)
    }

    Scaffold(
        topBar = {
            TopBarWithActions(
                title = uiState.candidacy?.offerTitle.orEmpty(),
                onNavigationClick = { viewModel.handle(CandidacyDetailEvent.OnBackClick) },
                navigationIcon = Icons.Rounded.Close,
                centerTitle = true
            )
        },
        containerColor = AppColors.surface
    ) { padding ->
        val candidacy = uiState.candidacy

        if (candidacy == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Loader(viewModel)
            }
            return@Scaffold
        }

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 10.dp)
                    .padding(horizontal = SPACING_M)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(SPACING_S))

                val painter = rememberAsyncImagePainter(
                    model = candidacy.studentPhoto,
                    fallback = painterResource(id = R.drawable.default_avatar),
                    error = painterResource(id = R.drawable.default_avatar),
                    placeholder = painterResource(id = R.drawable.default_avatar)
                )

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(SPACING_S))

                Text(
                    text = "${candidacy.studentName} ${candidacy.studentSurname}",
                    style = AppTypography.titleLarge
                )
                Text(text = candidacy.studentEmail, style = AppTypography.bodyMedium)

                Spacer(modifier = Modifier.height(SPACING_M))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.candidacy_postulation_text),
                        style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = candidacy.postulationDate.format(formatter),
                        style = AppTypography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(SPACING_L))

                candidacy.presentationCard?.takeIf { it.isNotBlank() }?.let { card ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.candidacy_presentation_card_text),
                            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(SPACING_XS))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp, max = 150.dp)
                                .verticalScroll(rememberScrollState())
                                .background(AppColors.background)
                                .padding(SPACING_XS)
                        ) {
                            Text(text = card, style = AppTypography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(SPACING_M))
                    }
                }

                Spacer(modifier = Modifier.height(140.dp))
            }

            if (candidacy.status == CandidacyStatus.PENDING && !uiState.alreadyHandled) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(AppColors.surface)
                        .padding(horizontal = SPACING_M, vertical = SPACING_M)
                ) {
                    OutlinedTextField(
                        value = uiState.note,
                        onValueChange = { viewModel.handle(CandidacyDetailEvent.OnNoteChange(it)) },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.candidacy_additional_note_hint_text),
                                style = AppTypography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(SPACING_M))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SPACING_M)
                    ) {
                        Button(
                            onClick = { viewModel.handle(CandidacyDetailEvent.OnRejectClick) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.candidacy_reject_button))
                        }
                        Button(
                            onClick = { viewModel.handle(CandidacyDetailEvent.OnAcceptClick) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.candidacy_accept_button))
                        }
                    }
                }
            }
        }

        if (uiState.showAcceptDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.handle(CandidacyDetailEvent.OnDismissDialog) },
                title = {
                    Text(text = stringResource(id = R.string.candidacy_dialog_accept_title_text))
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.handle(CandidacyDetailEvent.OnConfirmAccept)
                    }) {
                        Text(text = stringResource(id = R.string.candidacy_dialog_accept_confirm_button))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.handle(CandidacyDetailEvent.OnDismissDialog)
                    }) {
                        Text(text = stringResource(id = R.string.candidacy_dialog_cancel_button))
                    }
                }
            )
        }

        if (uiState.showRejectDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.handle(CandidacyDetailEvent.OnDismissDialog) },
                title = {
                    Text(text = stringResource(id = R.string.candidacy_dialog_reject_title_text))
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.handle(CandidacyDetailEvent.OnConfirmReject)
                    }) {
                        Text(text = stringResource(id = R.string.candidacy_dialog_reject_confirm_button))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.handle(CandidacyDetailEvent.OnDismissDialog)
                    }) {
                        Text(text = stringResource(id = R.string.candidacy_dialog_cancel_button))
                    }
                }
            )
        }
    }
}
