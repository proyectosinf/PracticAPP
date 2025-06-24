package com.mobivery.fct25.feature.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.mobivery.fct25.feature.company.ui.createcompany.navigation.companyGraph
import com.mobivery.fct25.feature.company.ui.createcompany.navigation.CREATE_COMPANY_ROUTE
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.viewCompanyGraph

const val COMPANY_NESTED_NAVIGATION_ROUTE = "company_nested_navigation_route"

fun NavController.navigateToCompany() {
    this.navigate(COMPANY_NESTED_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.companyFeatureGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = CREATE_COMPANY_ROUTE,
        route = COMPANY_NESTED_NAVIGATION_ROUTE
    ) {
        companyGraph(navController)
        viewCompanyGraph(navController)
    }
}
