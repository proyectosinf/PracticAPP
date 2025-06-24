package com.mobivery.fct25.feature.components.ui.componentsbuttons.navigation

import com.mobivery.fct25.feature.components.ui.componentsbuttons.ComponentsButtonsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val COMPONENTS_BUTTONS_ROUTE = "components_buttons_route"

fun NavController.navigateToComponentsButtons() {
    this.navigate(COMPONENTS_BUTTONS_ROUTE)
}

fun NavGraphBuilder.componentsButtonsGraph(
    navController: NavController,
) {
    composable(COMPONENTS_BUTTONS_ROUTE) {
        ComponentsButtonsScreen(
            onBack = navController::popBackStack
        )
    }
}