package com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobivery.fct25.feature.student.ui.student.studentofferdetails.StudentOfferDetailsScreen
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.StudentPublishedOffersScreen

const val STUDENT_PUBLISHED_OFFERS_ROUTE = "student_published_offer_route"
const val STUDENT_OFFER_DETAIL_ROUTE = "student_offer_detail_route"
const val OFFER_ID_ARG = "offerId"

fun NavController.navigateToStudentPublishedOffer() {
    navigate(STUDENT_PUBLISHED_OFFERS_ROUTE)
}

fun NavController.navigateToStudentOfferDetails(offerId: Int) {
    navigate("$STUDENT_OFFER_DETAIL_ROUTE/$offerId")
}

fun NavGraphBuilder.studentGraph(navController: NavController) {
    composable(STUDENT_PUBLISHED_OFFERS_ROUTE) {
        StudentPublishedOffersScreen(navController)
    }
    composable(
        route = "$STUDENT_OFFER_DETAIL_ROUTE/{$OFFER_ID_ARG}",
        arguments = listOf(navArgument(OFFER_ID_ARG) { type = NavType.IntType })
    ) { backStackEntry ->
        val offerId = backStackEntry.arguments?.getInt(OFFER_ID_ARG) ?: return@composable
        StudentOfferDetailsScreen(
            offerId = offerId,
            navController = navController,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
