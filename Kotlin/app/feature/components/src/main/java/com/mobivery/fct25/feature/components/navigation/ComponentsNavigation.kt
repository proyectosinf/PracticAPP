package com.mobivery.fct25.feature.components.navigation

import com.mobivery.fct25.feature.components.ui.componentsbuttons.navigation.componentsButtonsGraph
import com.mobivery.fct25.feature.components.ui.componentsdialog.navigation.componentsDialogsGraph
import com.mobivery.fct25.feature.components.ui.componentstextfields.navigation.componentsTextFieldsGraph
import com.mobivery.fct25.feature.components.ui.menu.componentsnavigation.COMPONENTS_MENU_ROUTE
import com.mobivery.fct25.feature.components.ui.menu.componentsnavigation.componentsMenuGraph
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

const val COMPONENTS_FEATURE_NAVIGATION_ROUTE = "components"

fun NavGraphBuilder.componentsFeatureGraph(
    navController: NavController
){
    navigation(
        route = COMPONENTS_FEATURE_NAVIGATION_ROUTE,
        startDestination = COMPONENTS_MENU_ROUTE
    ) {
        componentsMenuGraph(navController)
        componentsButtonsGraph(navController)
        componentsTextFieldsGraph(navController)
        componentsDialogsGraph(navController)
    }
}