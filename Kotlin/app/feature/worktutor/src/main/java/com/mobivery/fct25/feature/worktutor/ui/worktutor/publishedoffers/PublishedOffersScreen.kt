package com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import com.mobivery.fct25.app.common.model.OfferItemUiModel
import com.mobivery.fct25.app.common.model.OfferStatus
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist.navigation.navigateToCandidaciesList
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.navigation.navigateToCreateOffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishedOffersScreen(
    navController: NavController,
    viewModel: PublishedOffersViewModelInterface = hiltViewModel<PublishedOffersViewModel>()
) {
    val uiState by viewModel.publishedOffersUiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.handle(PublishedOffersEvent.OnScreenLoad)
    }

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            is PublishedOffersNavigation.NavigateToCreateOffer -> {
                navController.navigateToCreateOffer()
                viewModel.handle(PublishedOffersEvent.DidNavigate)
            }

            is PublishedOffersNavigation.NavigateToCandidacies -> {
                navController.navigateToCandidaciesList(navigation.offerId.toInt())
                viewModel.handle(PublishedOffersEvent.DidNavigate)
            }

            null -> Unit
        }
    }

    LaunchedEffect(
        listState,
        uiState.isLoading,
        uiState.isRefreshing,
        uiState.isLoadingMore,
        uiState.hasNextPage
    ) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collectLatest { lastVisibleIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                val canLoadMore =
                    !uiState.isLoading && !uiState.isRefreshing && !uiState.isLoadingMore && uiState.hasNextPage
                if (canLoadMore && lastVisibleIndex != null && lastVisibleIndex >= totalItems - 3) {
                    viewModel.handle(PublishedOffersEvent.OnScrollToEnd)
                }
            }
    }

    LaunchedEffect(uiState.restoreScroll) {
        if (uiState.restoreScroll) {
            listState.scrollToItem(
                index = uiState.firstVisibleItemIndex,
                scrollOffset = uiState.firstVisibleItemOffset
            )
            viewModel.handle(PublishedOffersEvent.OnScrollRestored)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collectLatest { (index, offset) ->
            viewModel.handle(PublishedOffersEvent.OnScrollPositionChanged(index, offset))
        }
    }

    Scaffold(
        containerColor = AppColors.surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handle(PublishedOffersEvent.OnCreateOfferClick) },
                containerColor = AppColors.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(id = R.string.offer_list_my_offers_text),
                            style = AppTypography.titleLarge,
                            color = AppColors.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.surface)
            )
        }
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .background(AppColors.surface)
        ) {
            if (uiState.isLoading && !uiState.isRefreshing) {
                Loader(viewModel = viewModel)
            }

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.handle(PublishedOffersEvent.OnRefresh) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SPACING_M)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = SPACING_S),
                        horizontalArrangement = Arrangement.spacedBy(
                            SPACING_S,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        listOf(
                            OfferStatus.ACTIVE to stringResource(R.string.offer_list_open_text),
                            OfferStatus.CLOSED to stringResource(R.string.offer_list_close_text)
                        ).forEach { (status, label) ->
                            val selected = uiState.statusFilter == status
                            val backgroundColor =
                                if (selected) AppColors.primary else AppColors.onSurface.copy(alpha = 0.1f)
                            val contentColor = if (selected) Color.White else AppColors.onSurface

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = backgroundColor,
                                onClick = {
                                    viewModel.handle(
                                        PublishedOffersEvent.OnStatusFilterChange(
                                            status
                                        )
                                    )
                                }
                            ) {
                                Text(
                                    text = label,
                                    style = AppTypography.labelLarge,
                                    color = contentColor,
                                    modifier = Modifier.padding(
                                        horizontal = SPACING_M,
                                        vertical = SPACING_XS
                                    )
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(SPACING_S)
                    ) {
                        if (uiState.offers.isEmpty()) {
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
                                        Image(
                                            painter = painterResource(R.drawable.mail),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(96.dp)
                                                .height(96.dp)
                                                .padding(bottom = SPACING_S)
                                        )

                                        Text(
                                            text = stringResource(R.string.offer_list_no_offers_text),
                                            style = AppTypography.bodyMedium,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            items(uiState.offers) { offer ->
                                OfferItemCard(
                                    offer = offer,
                                    onClick = {
                                        viewModel.handle(PublishedOffersEvent.OnCandidacyClick(offer.id.toString()))
                                    }
                                )
                            }

                            if (uiState.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = SPACING_M),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.common_loading_text),
                                            style = AppTypography.bodySmall,
                                            color = AppColors.onSurfaceSecondary
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(SPACING_M))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(SPACING_M))
        }
    }
}


@Composable
private fun OfferItemCard(
    offer: OfferItemUiModel, onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = AppColors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }) {
        Column(modifier = Modifier.padding(SPACING_M)) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = offer.title,
                    style = AppTypography.titleMedium,
                    color = AppColors.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(SPACING_S))

                val statusColor = when (offer.status) {
                    OfferStatus.ACTIVE -> AppColors.success
                    OfferStatus.CLOSED -> AppColors.error
                    null -> AppColors.onSurfaceSecondary
                }

                val statusText = when (offer.status) {
                    OfferStatus.ACTIVE -> stringResource(id = R.string.offer_list_active_text)
                    OfferStatus.CLOSED -> stringResource(id = R.string.offer_list_closed_text)
                    null -> null
                }

                if (statusText != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = statusColor.copy(alpha = 0.2f),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            style = AppTypography.labelMedium,
                            modifier = Modifier.padding(
                                horizontal = SPACING_XS, vertical = SPACING_XXS
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(SPACING_S))

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(id = R.string.offer_formation_text))
                        append(" ")
                    }
                    append(offer.degree)
                }, style = AppTypography.bodySmall, color = AppColors.onSurfaceSecondary
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(id = R.string.offer_list_vacancies_text))
                        append(" ")
                    }
                    append(offer.vacancies.toString())
                }, style = AppTypography.bodySmall, color = AppColors.onSurfaceSecondary
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(id = R.string.offer_list_modality_text))
                        append(" ")
                    }
                    append(offer.typeLabel)
                },
                style = AppTypography.bodySmall,
                color = AppColors.onSurfaceSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                        ) {
                            append(stringResource(id = R.string.offer_list_watchers_text))
                            append(" ")
                        }
                        append(offer.views.toString())
                    },
                    style = AppTypography.bodySmall,
                    color = AppColors.onSurfaceSecondary
                )

                Text(
                    buildAnnotatedString {
                        withStyle(
                            AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                        ) {
                            append(stringResource(id = R.string.candidacy_inscriptions_text))
                            append(" ")
                        }
                        append(offer.inscriptionsCandidacy.toString())
                    },
                    style = AppTypography.bodySmall,
                    color = AppColors.onSurfaceSecondary
                )
            }
        }
    }
}
