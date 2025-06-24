package com.mobivery.fct25.feature.components.ui.componentsdialog.navigation

import com.mobivery.fct25.feature.components.ui.componentsdialog.ComponentsDialogsScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val COMPONENTS_DIALOGS_ROUTE = "components_dialogs_route"

fun NavController.navigateToComponentsDialogs() {
    this.navigate(COMPONENTS_DIALOGS_ROUTE)
}

fun NavGraphBuilder.componentsDialogsGraph(
    navController: NavController,
) {
    composable(COMPONENTS_DIALOGS_ROUTE) {
        ComponentsDialogsScreen(
            onBack = navController::popBackStack
        )
    }
}