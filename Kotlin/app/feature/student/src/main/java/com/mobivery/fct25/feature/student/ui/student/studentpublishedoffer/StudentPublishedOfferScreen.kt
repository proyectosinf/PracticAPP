package com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.rememberAsyncImagePainter
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.dialogs.LogoutDialog
import com.mobivery.fct25.app.common.designsystem.component.loader.Loader
import com.mobivery.fct25.app.common.designsystem.component.topbar.TopBarWithActionRight
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import com.mobivery.fct25.app.common.model.OfferItemUiModel
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.navigation.navigateToStudentOfferDetails
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.format.DateTimeFormatter

private val CARD_CORNER_RADIUS = 12.dp
private val VERIFIED_ICON_SIZE = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPublishedOffersScreen(
    navController: NavController,
    viewModel: StudentPublishedOffersViewModelInterface = hiltViewModel<StudentPublishedOffersViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val refreshOfferId = savedStateHandle?.remove<Int>("offer_registered_id")

    LaunchedEffect(refreshOfferId) {
        refreshOfferId?.let {
            viewModel.handle(StudentPublishedOffersEvent.OnReturnFromDetails(it))
        }
    }

    LaunchedEffect(Unit) { viewModel.handle(StudentPublishedOffersEvent.OnScreenLoad) }

    LaunchedEffect(uiState.navigation) {
        when (val nav = uiState.navigation) {
            is StudentPublishedOffersNavigation.NavigateToOfferDetails -> {
                navController.navigateToStudentOfferDetails(nav.offerId)
                viewModel.handle(StudentPublishedOffersEvent.DidNavigate)
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
            .collectLatest { lastVisibleIdx ->
                val total = listState.layoutInfo.totalItemsCount
                val canLoadMore = !uiState.isLoading &&
                        !uiState.isRefreshing &&
                        !uiState.isLoadingMore &&
                        uiState.hasNextPage
                if (canLoadMore &&
                    lastVisibleIdx != null &&
                    lastVisibleIdx >= total - 3
                ) {
                    viewModel.handle(StudentPublishedOffersEvent.OnScrollToEnd)
                }
            }
    }

    LaunchedEffect(uiState.restoreScroll) {
        if (uiState.restoreScroll) {
            listState.scrollToItem(
                index = uiState.firstVisibleItemIndex,
                scrollOffset = uiState.firstVisibleItemOffset
            )
            viewModel.handle(StudentPublishedOffersEvent.OnScrollRestored)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collectLatest { (idx, offset) ->
            viewModel.handle(StudentPublishedOffersEvent.OnScrollPositionChanged(idx, offset))
        }
    }

    Scaffold(
        containerColor = AppColors.surface,
        topBar = {
            TopBarWithActionRight(
                title = stringResource(R.string.offer_list_offer_list_nav_text),
                centerTitle = true,
                onRightActionClick = {
                    viewModel.handle(StudentPublishedOffersEvent.RequestLogoutConfirmation)
                }
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
            if (uiState.offers.isEmpty() && uiState.isLoading && !uiState.isRefreshing) {
                Loader(viewModel = viewModel)
            }

            if (uiState.showLogoutConfirmationDialog) {
                LogoutDialog(
                    title = stringResource(R.string.company_logout_text_text),
                    message = stringResource(R.string.company_areyousure_text),
                    onConfirmLogout = { viewModel.handle(StudentPublishedOffersEvent.ConfirmLogout) },
                    onCancel = { viewModel.handle(StudentPublishedOffersEvent.CancelLogout) },
                    onDismiss = { viewModel.handle(StudentPublishedOffersEvent.CancelLogout) }
                )
            }

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.handle(StudentPublishedOffersEvent.OnPullToRefresh) },
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
                        if (uiState.offers.isEmpty() && !uiState.isLoading) {
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
                                        viewModel.handle(
                                            StudentPublishedOffersEvent.OnOfferClick(offer.id)
                                        )
                                    }
                                )
                            }
                            item { Spacer(Modifier.height(SPACING_XXS)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferItemCard(
    offer: OfferItemUiModel,
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
        Column(Modifier.padding(SPACING_M)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SPACING_S),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!offer.logoUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(offer.logoUrl),
                        contentDescription = stringResource(R.string.offer_list_icon_description_text),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AppColors.surface)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )
                }

                Text(
                    text = offer.title,
                    style = AppTypography.titleMedium,
                    color = AppColors.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(SPACING_S))

            offer.company?.let {
                Text(
                    text = it,
                    style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.onSurfaceSecondary
                )
            }

            Spacer(Modifier.height(SPACING_S))

            offer.startDate?.let { start ->
                offer.endDate?.let { end ->
                    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                AppTypography.bodySmall.toSpanStyle()
                                    .copy(fontWeight = FontWeight.Bold)
                            ) {
                                append(stringResource(R.string.offer_list_date_offer_text))
                                append(" ")
                            }
                            append("${start.format(dateFormatter)} – ${end.format(dateFormatter)}")
                        },
                        style = AppTypography.bodySmall,
                        color = AppColors.onSurfaceSecondary
                    )
                }
            }

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(R.string.offer_formation_text))
                        append(" ")
                    }
                    append(offer.degree)
                },
                style = AppTypography.bodySmall,
                color = AppColors.onSurfaceSecondary
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(R.string.offer_list_vacancies_text))
                        append(" ")
                    }
                    append(offer.vacancies.toString())
                },
                style = AppTypography.bodySmall,
                color = AppColors.onSurfaceSecondary
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        AppTypography.bodySmall.toSpanStyle().copy(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(R.string.offer_list_modality_text))
                        append(" ")
                    }
                    append(offer.typeLabel)
                },
                style = AppTypography.bodySmall,
                color = AppColors.onSurfaceSecondary
            )

            if (offer.isEnrolled) {
                Spacer(Modifier.height(SPACING_S))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_verified),
                        contentDescription = stringResource(R.string.offer_list_icon_inscribe_text),
                        modifier = Modifier
                            .size(VERIFIED_ICON_SIZE)
                            .padding(end = SPACING_XXS)
                    )
                    Text(
                        text = stringResource(R.string.offer_list_inscribe_text),
                        style = AppTypography.bodySmall,
                        color = AppColors.success
                    )
                }
            }
        }
    }
}
