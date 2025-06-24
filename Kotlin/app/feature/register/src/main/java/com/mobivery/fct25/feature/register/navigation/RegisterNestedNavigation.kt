package com.mobivery.fct25.feature.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mobivery.fct25.feature.register.ui.register.RegisterScreen
import com.mobivery.fct25.feature.register.ui.register.navigation.REGISTER_NAVIGATION_ROUTE


const val REGISTER_NESTED_NAVIGATION_ROUTE = "register_nested_route"

fun NavController.navigateToRegister(navOptions: NavOptions? = null) {
    this.navigate(REGISTER_NESTED_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.registerFeatureGraph(
    navController: NavController
) {
    navigation(
        startDestination = REGISTER_NAVIGATION_ROUTE,
        route = REGISTER_NESTED_NAVIGATION_ROUTE
    ) {
        registerScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.registerScreen(
    onNavigateBack: () -> Unit
) {
    composable(REGISTER_NAVIGATION_ROUTE) {
        RegisterScreen(onNavigateBack = onNavigateBack)
    }
}
