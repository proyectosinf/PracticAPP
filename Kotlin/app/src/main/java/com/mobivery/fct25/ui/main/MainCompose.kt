package com.mobivery.fct25.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.HEIGHT_ICON_BOTTOM_BAR
import com.mobivery.fct25.feature.about.navigation.aboutSection
import com.mobivery.fct25.feature.company.ui.createcompany.navigation.CREATE_COMPANY_ROUTE
import com.mobivery.fct25.feature.company.ui.createcompany.navigation.companyGraph
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.VIEW_COMPANY_ROUTE
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.viewCompanyGraph
import com.mobivery.fct25.feature.components.navigation.componentsFeatureGraph
import com.mobivery.fct25.feature.forgotpassword.navigation.FORGOT_PASSWORD_ROUTE
import com.mobivery.fct25.feature.forgotpassword.navigation.forgotPasswordGraph
import com.mobivery.fct25.feature.home.navigation.homeSection
import com.mobivery.fct25.feature.login.navigation.LOGIN_NESTED_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.login.navigation.loginFeatureGraph
import com.mobivery.fct25.feature.login.navigation.navigateToLogin
import com.mobivery.fct25.feature.login.ui.login.navigation.LOGIN_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.register.navigation.navigateToRegister
import com.mobivery.fct25.feature.register.navigation.registerFeatureGraph
import com.mobivery.fct25.feature.register.ui.register.navigation.REGISTER_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentcandidacies.navigation.studentCandidaciesGraph
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.navigation.STUDENT_CANDIDACY_DETAIL_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.navigation.studentCandidacyDetailGraph
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.navigation.STUDENT_OFFER_DETAILS_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.navigation.studentOfferDetailsGraph
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.STUDENT_PUBLISHED_OFFERS_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.studentGraph
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist.navigation.CANDIDACIES_LIST_ROUTE
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist.navigation.candidaciesListScreen
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.navigation.CANDIDACY_DETAIL_ROUTE
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.navigation.candidacyDetailGraph
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.navigation.CREATE_OFFER_ROUTE
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.navigation.createOfferGraph
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation.publishedOffersGraph
import com.mobivery.fct25.model.SectionUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainCompose(
    onExitApp: () -> Unit,
    viewModel: MainViewModelInterface = hiltViewModel<MainViewModel>(),
) {
    val navController = rememberNavController()
    val mainUiState by viewModel.mainUiState.collectAsStateWithLifecycle()
    var showBottomBar by remember { mutableStateOf(BottomBarType.HIDDEN) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ObserveBottomNavigationVisibility(navController) { show ->
        showBottomBar = if (show) BottomBarType.VISIBLE else BottomBarType.HIDDEN
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar == BottomBarType.VISIBLE) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color.DarkGray
                ) {
                    val navigationBarItemColors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = AppColors.primary,
                        selectedTextColor = AppColors.primary,
                        unselectedIconColor = Color.DarkGray,
                        unselectedTextColor = Color.DarkGray,
                    )

                    mainUiState.sections.forEach { screen ->
                        val selected = if (screen.route == "candidacies_route") {
                            currentRoute == screen.route
                        } else {
                            currentRoute == screen.route
                        }

                        NavigationBarItem(
                            selected = selected,
                            colors = navigationBarItemColors,
                            icon = {
                                Icon(
                                    painter = painterResource(id = screen.iconResourceId),
                                    contentDescription = null,
                                    modifier = Modifier.size(HEIGHT_ICON_BOTTOM_BAR)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(screen.labelResourceId),
                                    style = AppTypographies.bodySmall,
                                )
                            },
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = screen.restoreState
                                }
                                screen.restoreState = true
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (mainUiState.isUserLogged != null) {
            NavHost(
                navController = navController,
                startDestination = when {
                    mainUiState.userLocal?.role == 1 -> STUDENT_PUBLISHED_OFFERS_ROUTE
                    mainUiState.userLocal?.role == 2 && mainUiState.userLocal?.companyId == null -> CREATE_COMPANY_ROUTE
                    mainUiState.userLocal?.role == 2 && mainUiState.userLocal?.companyId != null -> VIEW_COMPANY_ROUTE
                    else -> LOGIN_NESTED_NAVIGATION_ROUTE
                },
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                loginFeatureGraph(
                    navController = navController,
                    onExitApp = { onExitApp() },
                    onNavigateToRegister = {
                        navController.navigateToRegister()
                    }
                )
                registerFeatureGraph(navController)
                homeSection(navController, navController::navigateToLogin)
                aboutSection(navController)
                componentsFeatureGraph(navController)
                companyGraph(navController)
                viewCompanyGraph(navController)
                publishedOffersGraph(navController)
                createOfferGraph(navController)
                studentGraph(navController)
                studentOfferDetailsGraph(navController)
                candidaciesListScreen(navController)
                studentCandidaciesGraph(navController)
                candidacyDetailGraph(navController)
                studentCandidacyDetailGraph(navController)
                forgotPasswordGraph(navController)
            }

            LaunchedEffect(mainUiState.navigateToLogin) {
                if (mainUiState.navigateToLogin) {
                    navController.navigateToLogin()
                    viewModel.handle(MainEvent.OnNavigatedToLogin)
                }
            }
        }
    }
}

@Composable
fun ObserveBottomNavigationVisibility(
    navController: NavController,
    onVisibilityChange: (Boolean) -> Unit,
) {
    val hiddenRoutes = setOf(
        LOGIN_NAVIGATION_ROUTE,
        REGISTER_NAVIGATION_ROUTE,
        CREATE_COMPANY_ROUTE,
        CREATE_OFFER_ROUTE,
        STUDENT_OFFER_DETAILS_ROUTE,
        CANDIDACIES_LIST_ROUTE,
        CANDIDACY_DETAIL_ROUTE,
        STUDENT_CANDIDACY_DETAIL_ROUTE,
        FORGOT_PASSWORD_ROUTE,
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val cleanedRoute = currentRoute
        ?.substringBefore("/") // Remove path arguments
        ?.substringBefore("?") // Remove query arguments
    val isHidden = cleanedRoute in hiddenRoutes || currentRoute == null

    LaunchedEffect(currentRoute) {
        onVisibilityChange(!isHidden)
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun MainComposePreview() {
    AppTheme {
        MainCompose(
            onExitApp = {},
            viewModel = composePreviewViewModel
        )
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun MainComposeDarkPreview() {
    AppTheme(darkTheme = true) {
        MainCompose(
            onExitApp = {},
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : MainViewModelInterface {
        override val mainUiState: StateFlow<MainUiState>
            get() = MutableStateFlow(
                MainUiState(
                    sections = listOfNotNull(
                        SectionUiModel.Home,
                        SectionUiModel.About,
                    )
                )
            )

        override fun handle(event: MainEvent) {
            when (event) {
                MainEvent.OnNavigatedToLogin -> {}
            }
        }

        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        override fun closeError() {}
    }
}

enum class BottomBarType {
    VISIBLE, HIDDEN,
}
