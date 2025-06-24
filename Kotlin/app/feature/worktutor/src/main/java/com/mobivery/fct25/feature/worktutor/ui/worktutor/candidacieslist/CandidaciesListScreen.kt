package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithLeftCancel
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.template.domain.model.candidacy.Candidacy
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

private val CARD_CORNER_RADIUS = 12.dp
private val TAG_HORIZONTAL_PADDING = 12.dp
private val TAG_VERTICAL_PADDING = 4.dp
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidaciesListScreen(
    offerId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToCandidacyDetail: (Int) -> Unit,
    viewModel: CandidaciesListViewModelInterface = hiltViewModel<CandidaciesListViewModel>()
) {
    val uiState by viewModel.candidaciesListUiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.handle(CandidaciesListEvent.OnLoadIfNeeded(offerId))
    }

    LaunchedEffect(uiState.navigation) {
        when (val nav = uiState.navigation) {
            is CandidaciesListNavigation.NavigateToCandidacyDetail -> {
                onNavigateToCandidacyDetail(nav.candidacyId)
                viewModel.handle(CandidaciesListEvent.DidNavigate)
            }

            null -> Unit
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisible ->
                lastVisible?.let {
                    viewModel.handle(CandidaciesListEvent.OnScrollPositionChanged(it))
                }
            }
    }

    Scaffold(
        topBar = {
            TopBarWithLeftCancel(
                title = stringResource(R.string.candidacy_title_text),
                onCancelClick = onNavigateBack
            )
        },
        containerColor = AppColors.surface
    ) { innerPadding ->

        val topPadding = innerPadding.calculateTopPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .background(AppColors.surface)
        ) {
            if (uiState.isLoading && uiState.candidacies.isEmpty()) Loader(viewModel = viewModel)

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.handle(CandidaciesListEvent.OnRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SPACING_M)
                ) {
                    Spacer(Modifier.height(SPACING_S))

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(SPACING_S)
                    ) {
                        if (uiState.candidacies.isEmpty() && !uiState.isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(SPACING_XL),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        androidx.compose.foundation.Image(
                                            painter = painterResource(R.drawable.users_3),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(96.dp)
                                                .height(96.dp)
                                                .padding(bottom = SPACING_S)
                                        )

                                        Text(
                                            text = stringResource(R.string.candidacy_not_candidacy_info_text),
                                            style = AppTypography.bodyMedium,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            items(uiState.candidacies) { candidacy ->
                                CandidacyItemCard(
                                    candidacy = candidacy,
                                    onClick = {
                                        viewModel.handle(
                                            CandidaciesListEvent.OnCandidacyClick(candidacy.id)
                                        )
                                    }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(SPACING_XS))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidacyItemCard(
    candidacy: Candidacy,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = AppColors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(SPACING_M)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${candidacy.studentName} ${candidacy.studentSurname}",
                        style = AppTypography.titleMedium,
                        color = AppColors.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                val (statusColor, statusLabel) = when (candidacy.status) {
                    CandidacyStatus.PENDING -> AppColors.warning to R.string.candidacy_status_pending_text
                    CandidacyStatus.ACCEPTED -> AppColors.success to R.string.candidacy_status_accepted_text
                    CandidacyStatus.REJECTED -> AppColors.error to R.string.candidacy_status_rejected_text
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = 0.20f),
                    modifier = Modifier.padding(start = SPACING_S)
                ) {
                    Text(
                        text = stringResource(statusLabel),
                        color = statusColor,
                        style = AppTypography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = TAG_HORIZONTAL_PADDING,
                            vertical = TAG_VERTICAL_PADDING
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(SPACING_S))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.candidacy_postulation_text),
                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.onSurfaceSecondary
                )

                Text(
                    text = candidacy.postulationDate.format(DATE_FORMATTER),
                    style = AppTypography.bodySmall,
                    color = AppColors.onSurfaceSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
