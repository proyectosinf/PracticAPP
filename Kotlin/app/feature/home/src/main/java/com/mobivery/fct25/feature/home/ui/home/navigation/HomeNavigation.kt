package com.mobivery.fct25.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.home.ui.home.HomeScreen

const val HOME_FEATURE_NAVIGATION_ROUTE = "home"

fun NavController.navigateToHome(builder: NavOptionsBuilder.() -> Unit) {
    this.navigate(HOME_FEATURE_NAVIGATION_ROUTE, builder)
}

fun NavGraphBuilder.homeSection(
    navController: NavHostController,
    goToLogin: () -> Unit,
) {
    composable(HOME_FEATURE_NAVIGATION_ROUTE) {
        HomeScreen(
            goToLogin = goToLogin,
        )
    }
}