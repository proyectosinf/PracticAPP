package com.mobivery.fct25.feature.company.ui.viewCompany.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.company.ui.viewCompany.ViewCompanyScreen

const val VIEW_COMPANY_ROUTE = "view_company_route"

fun NavController.navigateToViewCompany() {
    this.navigate(VIEW_COMPANY_ROUTE)
}

fun NavGraphBuilder.viewCompanyGraph(
    navController: NavController,
) {
    composable(VIEW_COMPANY_ROUTE) {
        ViewCompanyScreen()
    }
}
