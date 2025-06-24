package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacieslist.CandidaciesListScreen

const val CANDIDACIES_LIST_ROUTE = "candidacies_list_route"
private const val ARG_OFFER_ID = "offerId"
private const val CANDIDACIES_LIST_ROUTE_WITH_ARG = "$CANDIDACIES_LIST_ROUTE/{$ARG_OFFER_ID}"

fun NavController.navigateToCandidaciesList(offerId: Int) =
    navigate("$CANDIDACIES_LIST_ROUTE/$offerId")

fun NavGraphBuilder.candidaciesListScreen(navController: NavController) {
    composable(
        route = CANDIDACIES_LIST_ROUTE_WITH_ARG,
        arguments = listOf(navArgument(ARG_OFFER_ID) { type = NavType.IntType })
    ) { backStackEntry ->
        val offerId = backStackEntry.arguments?.getInt(ARG_OFFER_ID) ?: return@composable
        CandidaciesListScreen(
            offerId = offerId,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCandidacyDetail = { id ->
                navController.navigate("candidacy_detail_route/$id")
            }
        )
    }
}
