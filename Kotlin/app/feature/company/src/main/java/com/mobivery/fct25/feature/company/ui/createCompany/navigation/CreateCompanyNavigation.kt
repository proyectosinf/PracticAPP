package com.mobivery.fct25.feature.company.ui.createcompany.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.company.ui.createcompany.CreateCompanyScreen

const val CREATE_COMPANY_ROUTE = "create_company_route"

fun NavController.navigateToCompany() {
    this.navigate(CREATE_COMPANY_ROUTE)
}

fun NavGraphBuilder.companyGraph(
    navController: NavHostController,
) {
    composable(CREATE_COMPANY_ROUTE) {
        CreateCompanyScreen(navController = navController)
    }
}
