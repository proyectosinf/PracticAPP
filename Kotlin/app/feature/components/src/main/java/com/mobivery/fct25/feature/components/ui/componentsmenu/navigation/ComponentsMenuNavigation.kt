package com.mobivery.fct25.feature.components.ui.menu.componentsnavigation

import com.mobivery.fct25.feature.components.ui.componentsbuttons.navigation.navigateToComponentsButtons
import com.mobivery.fct25.feature.components.ui.componentsdialog.navigation.navigateToComponentsDialogs
import com.mobivery.fct25.feature.components.ui.componentsmenu.ComponentsMenuScreen
import com.mobivery.fct25.feature.components.ui.componentstextfields.navigation.navigateToComponentsTextFields
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val COMPONENTS_MENU_ROUTE = "components_menu_route"

fun NavController.navigateToComponentsMenu() {
    this.navigate(COMPONENTS_MENU_ROUTE)
}

fun NavGraphBuilder.componentsMenuGraph(
    navController: NavController,
) {
    composable(COMPONENTS_MENU_ROUTE) {
        ComponentsMenuScreen(
            navigateToComponentsButtons = navController::navigateToComponentsButtons,
            navigateToComponentsTextFields = navController::navigateToComponentsTextFields,
            navigateToDialogs = navController::navigateToComponentsDialogs
        )
    }
}