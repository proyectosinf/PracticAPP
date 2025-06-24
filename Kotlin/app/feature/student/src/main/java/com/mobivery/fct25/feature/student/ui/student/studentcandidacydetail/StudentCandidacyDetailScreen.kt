package com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.StudentCandidacyDetailViewModel.StudentCandidacyDetailEvent
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.StudentCandidacyDetailViewModel.StudentCandidacyDetailNavigation
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCandidacyDetailScreen(
    navController: NavController,
) {
    val viewModel: StudentCandidacyDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (uiState.navigation) {
            StudentCandidacyDetailNavigation.NavigateBack -> {
                navController.popBackStack()
                viewModel.handle(StudentCandidacyDetailEvent.OnNavigationHandled)
            }

            null -> Unit
        }
    }

    BackHandler {
        viewModel.handle(StudentCandidacyDetailEvent.OnBackClick)
    }

    Scaffold(
        topBar = {
            TopBarWithActions(
                title = uiState.candidacy?.offerTitle.orEmpty(),
                onNavigationClick = { viewModel.handle(StudentCandidacyDetailEvent.OnBackClick) },
                navigationIcon = Icons.Rounded.Close,
                centerTitle = true
            )
        },
        containerColor = AppColors.surface
    ) { padding ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Loader(viewModel)
            }
        } else {
            val candidacy = uiState.candidacy ?: return@Scaffold

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = SPACING_M)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(SPACING_S))

                val painter = if (!candidacy.companyPhoto.isNullOrBlank()) {
                    rememberAsyncImagePainter(model = candidacy.companyPhoto)
                } else {
                    painterResource(id = R.drawable.default_avatar)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    )
                }

                Spacer(modifier = Modifier.height(SPACING_S))

                Text(
                    text = candidacy.companyName,
                    style = AppTypography.titleLarge
                )

                Spacer(modifier = Modifier.height(SPACING_XS))

                candidacy.contactName?.takeIf { it.isNotBlank() }?.let {
                    Text(text = it, style = AppTypography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(SPACING_XS))

                Text(
                    text = candidacy.companyEmail,
                    style = AppTypography.bodyMedium
                )

                Spacer(modifier = Modifier.height(SPACING_XS))

                candidacy.contactPhone?.takeIf { it.isNotBlank() }?.let {
                    Text(text = it, style = AppTypography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(SPACING_XL))

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

                candidacy.additionalNotes?.takeIf { it.isNotBlank() }?.let {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.candidacy_additional_note_text),
                            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(SPACING_XS))
                        Text(
                            text = it,
                            style = AppTypography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(SPACING_L))
                    }
                }
            }
        }
    }
}
