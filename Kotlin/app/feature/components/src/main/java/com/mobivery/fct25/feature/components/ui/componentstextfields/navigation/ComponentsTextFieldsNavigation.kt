package com.mobivery.fct25.feature.components.ui.componentstextfields.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.components.ui.componentstextfields.ComponentsTextFieldsScreen

internal const val COMPONENTS_TEXT_FIELDS_ROUTE = "components_text_fields_route"

fun NavController.navigateToComponentsTextFields() {
    this.navigate(COMPONENTS_TEXT_FIELDS_ROUTE)
}

fun NavGraphBuilder.componentsTextFieldsGraph(
    navController: NavController,
) {
    composable(COMPONENTS_TEXT_FIELDS_ROUTE) {
        ComponentsTextFieldsScreen(
            onBack = navController::popBackStack
        )
    }
}