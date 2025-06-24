package com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobivery.fct25.feature.student.ui.student.studentcandidacydetail.StudentCandidacyDetailScreen

const val STUDENT_CANDIDACY_DETAIL_ROUTE = "student_candidacy_detail_route"
private const val ARG_CANDIDACY_ID = "candidacyId"

fun NavController.navigateToStudentCandidacyDetail(candidacyId: Int) {
    this.navigate("$STUDENT_CANDIDACY_DETAIL_ROUTE/$candidacyId")
}

fun NavGraphBuilder.studentCandidacyDetailGraph(navController: NavController) {
    composable(
        route = "$STUDENT_CANDIDACY_DETAIL_ROUTE/{$ARG_CANDIDACY_ID}",
        arguments = listOf(navArgument(ARG_CANDIDACY_ID) { type = NavType.IntType })
    ) {
        StudentCandidacyDetailScreen(navController = navController)
    }
}
