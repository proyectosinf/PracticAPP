package com.mobivery.fct25.feature.login.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.login.ui.login.LoginScreen

const val LOGIN_NAVIGATION_ROUTE = "login_navigation_route"

fun NavController.navigateToLogin() {
    this.navigate(LOGIN_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.loginScreen(
    navController: NavHostController,
    onExitApp: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    composable(route = LOGIN_NAVIGATION_ROUTE) {
        LoginScreen(
            navController = navController,
            onExitApp = onExitApp,
            onNavigateToRegister = onNavigateToRegister
        )
    }
}
