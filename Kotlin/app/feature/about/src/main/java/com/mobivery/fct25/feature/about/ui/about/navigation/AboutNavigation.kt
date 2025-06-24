package com.mobivery.fct25.feature.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.about.ui.about.AboutScreen

const val ABOUT_SECTION_ROUTE = "about"

fun NavController.navigateToAbout(builder: NavOptionsBuilder.() -> Unit) {
    this.navigate(ABOUT_SECTION_ROUTE, builder)
}

fun NavGraphBuilder.aboutSection(
    navController: NavHostController,
) {
    composable(ABOUT_SECTION_ROUTE) {
        AboutScreen()
    }
}