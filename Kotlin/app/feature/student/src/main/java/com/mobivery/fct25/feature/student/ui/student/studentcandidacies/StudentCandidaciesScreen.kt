package com.mobivery.fct25.feature.student.ui.student.studentcandidacies

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavController
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.navigation.navigateToStudentCandidacyDetail
import com.mobivery.template.domain.model.candidacy.Candidacy
import com.mobivery.template.domain.model.candidacy.CandidacyStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

private val DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCandidaciesScreen(
    navController: NavController,
    viewModel: StudentCandidaciesViewModelInterface = hiltViewModel<StudentCandidaciesViewModel>()
) {
    val uiState by viewModel.studentCandidaciesUiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.restoreScroll) {
        if (uiState.restoreScroll) {
            listState.scrollToItem(
                index = uiState.firstVisibleItemIndex,
                scrollOffset = uiState.firstVisibleItemOffset
            )
            viewModel.handle(StudentCandidaciesEvent.OnScrollRestored)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collectLatest { (index, offset) ->
            viewModel.handle(StudentCandidaciesEvent.OnScrollPositionChanged(index, offset))
        }
    }

    LaunchedEffect(uiState.hasLoadedOnce) {
        if (!uiState.hasLoadedOnce) {
            viewModel.handle(StudentCandidaciesEvent.OnForceRefresh)
        }
    }

    LaunchedEffect(uiState.navigation) {
        when (val nav = uiState.navigation) {
            is StudentCandidaciesNavigation.NavigateToCandidacyDetail -> {
                navController.navigateToStudentCandidacyDetail(nav.candidacyId)
                viewModel.handle(StudentCandidaciesEvent.DidNavigate)
            }

            null -> { /* Do nothing */
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .map { it ?: 0 }
            .distinctUntilChanged()
            .collectLatest { last ->
                if (last >= uiState.candidacies.lastIndex) {
                    viewModel.handle(StudentCandidaciesEvent.OnScrollToEnd)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        Text(
                            stringResource(R.string.candidacy_title_text),
                            style = AppTypography.titleLarge,
                            color = AppColors.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.surface)
            )
        },
        containerColor = AppColors.surface
    ) { padding ->
        val top = padding.calculateTopPadding()
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = top)
                .background(AppColors.surface)
        ) {
            if (uiState.isLoading && !uiState.isRefreshing) {
                Loader(viewModel)
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = SPACING_M)
            ) {
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.handle(StudentCandidaciesEvent.OnPullToRefresh) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier.fillMaxSize()
                    ) {
                        Spacer(Modifier.height(SPACING_S))
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(SPACING_S),
                            modifier = Modifier.weight(1f)
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
                                                text = stringResource(R.string.candidacy_not_candidacy_text),
                                                style = AppTypography.bodyMedium,
                                                color = Color.DarkGray,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(uiState.candidacies) { candidacy ->
                                    CandidacyCard(candidacy) {
                                        viewModel.handle(
                                            StudentCandidaciesEvent.OnCandidacyClick(
                                                candidacy.id
                                            )
                                        )
                                    }
                                }
                                if (uiState.isLoadingMore) {
                                    item {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = SPACING_M),
                                            Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(24.dp),
                                                color = AppColors.primary
                                            )
                                        }
                                    }
                                } else {
                                    item { Spacer(Modifier.height(SPACING_XXS)) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidacyCard(candidacy: Candidacy, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = AppColors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(SPACING_M)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    candidacy.offerTitle,
                    style = AppTypography.titleMedium,
                    color = AppColors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                val (statusColor, statusText) = when (candidacy.status) {
                    CandidacyStatus.PENDING -> AppColors.warning to stringResource(R.string.candidacy_status_pending_text)
                    CandidacyStatus.ACCEPTED -> AppColors.success to stringResource(R.string.candidacy_status_accepted_text)
                    CandidacyStatus.REJECTED -> AppColors.error to stringResource(R.string.candidacy_status_rejected_text)
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusColor.copy(alpha = .2f)
                ) {
                    Text(
                        statusText,
                        color = statusColor,
                        style = AppTypography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = SPACING_S,
                            vertical = SPACING_XXS
                        )
                    )
                }
            }
            Spacer(Modifier.height(SPACING_S))
            Text(
                candidacy.companyName,
                style = AppTypography.bodyMedium,
                color = AppColors.onSurface
            )
            Spacer(Modifier.height(SPACING_S))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.candidacy_postulation_text),
                    color = AppColors.onSurfaceSecondary,
                    style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    candidacy.postulationDate.format(DATE_FMT),
                    color = AppColors.onSurfaceSecondary,
                    style = AppTypography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
