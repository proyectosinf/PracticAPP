package com.mobivery.fct25.feature.student.ui.student.studentofferdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.StudentOfferDetailsScreen

const val STUDENT_OFFER_DETAILS_ROUTE = "student_offer_details_route"
const val STUDENT_OFFER_DETAILS_ARG_ID = "offerId"
const val RETURNED_OFFER_ID_KEY = "returned_offer_id"
const val STUDENT_OFFER_DETAILS_ROUTE_WITH_ARG =
    "$STUDENT_OFFER_DETAILS_ROUTE/{$STUDENT_OFFER_DETAILS_ARG_ID}"

fun NavController.navigateToStudentOfferDetails(offerId: Int) {
    this.navigate("$STUDENT_OFFER_DETAILS_ROUTE/$offerId")
}

fun NavGraphBuilder.studentOfferDetailsGraph(
    navController: NavController,
) {
    composable(
        route = STUDENT_OFFER_DETAILS_ROUTE_WITH_ARG,
        arguments = listOf(navArgument(STUDENT_OFFER_DETAILS_ARG_ID) {
            type = NavType.IntType
        })
    ) { backStackEntry ->
        val offerId = backStackEntry.arguments?.getInt(STUDENT_OFFER_DETAILS_ARG_ID)
            ?: return@composable

        StudentOfferDetailsScreen(
            offerId = offerId,
            navController = navController,
            onNavigateBack = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(RETURNED_OFFER_ID_KEY, offerId)
                navController.popBackStack()
            }
        )
    }
}
