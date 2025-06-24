package com.mobivery.fct25.feature.student.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.studentGraph
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.STUDENT_PUBLISHED_OFFERS_ROUTE

const val STUDENT_NESTED_NAVIGATION_ROUTE = "student_nested_navigation_route"

fun NavController.navigateToStudent() {
    this.navigate(STUDENT_NESTED_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.studentFeatureGraph(
    navController: NavController,
) {
    navigation(
        startDestination = STUDENT_PUBLISHED_OFFERS_ROUTE,
        route = STUDENT_NESTED_NAVIGATION_ROUTE
    ) {
        studentGraph(navController)
    }
}
