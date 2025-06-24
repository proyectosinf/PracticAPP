package com.mobivery.fct25.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.mobivery.fct25.feature.login.ui.login.navigation.LOGIN_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.login.ui.login.navigation.loginScreen

/**
 *  Destinations can be grouped into a nested graph to modularize a particular flow in your app’s UI.
 *  An example of this could be a self-contained login flow.
 *  The nested graph encapsulates its destinations.
 *  As with the root graph, a nested graph must have a destination identified as the
 *  start destination by its route. This is the destination that is navigated to
 *  when you navigate to the route associated with the nested graph.
 *  https://developer.android.com/jetpack/compose/navigation#nested-nav
 */

const val LOGIN_NESTED_NAVIGATION_ROUTE = "login_nested_route"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    this.navigate(LOGIN_NESTED_NAVIGATION_ROUTE, navOptions)
}

fun NavGraphBuilder.loginFeatureGraph(
    navController: NavController,
    onNavigateToRegister: () -> Unit,
    onExitApp: () -> Unit
) {
    navigation(startDestination = LOGIN_NAVIGATION_ROUTE, route = LOGIN_NESTED_NAVIGATION_ROUTE) {
        loginScreen(navController as NavHostController, onExitApp, onNavigateToRegister)
    }
}