package com.mobivery.fct25.feature.register.ui.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.register.ui.register.RegisterScreen

const val REGISTER_NAVIGATION_ROUTE = "register_navigation_route"

fun NavGraphBuilder.registerScreen(navController: NavController) {
    composable(route = REGISTER_NAVIGATION_ROUTE) {
        RegisterScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
