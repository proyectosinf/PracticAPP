package com.mobivery.fct25.feature.forgotpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.forgotpassword.ForgotPasswordScreen

const val FORGOT_PASSWORD_ROUTE = "forgot_password_route"

fun NavController.navigateToForgotPassword() {
    this.navigate(FORGOT_PASSWORD_ROUTE)
}

fun NavGraphBuilder.forgotPasswordGraph(
    navController: NavController,
) {
    composable(FORGOT_PASSWORD_ROUTE) {
        ForgotPasswordScreen(
            onNavigateToLogin = {
                navController.popBackStack()
            }
        )
    }
}
