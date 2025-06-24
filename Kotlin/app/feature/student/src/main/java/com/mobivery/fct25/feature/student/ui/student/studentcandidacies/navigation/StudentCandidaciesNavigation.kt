package com.mobivery.fct25.feature.student.ui.student.studentcandidacies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.student.ui.student.studentcandidacies.StudentCandidaciesScreen

const val STUDENT_CANDIDACIES_ROUTE = "student_candidacies_route"

fun NavController.navigateToStudentCandidacies() {
    navigate(STUDENT_CANDIDACIES_ROUTE)
}

fun NavGraphBuilder.studentCandidaciesGraph(navController: NavController) {
    composable(STUDENT_CANDIDACIES_ROUTE) {
        StudentCandidaciesScreen(navController = navController)
    }
}
