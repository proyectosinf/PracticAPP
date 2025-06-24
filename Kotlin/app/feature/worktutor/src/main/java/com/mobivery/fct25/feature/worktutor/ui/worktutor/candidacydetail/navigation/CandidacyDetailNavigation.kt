package com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobivery.fct25.feature.worktutor.ui.worktutor.candidacydetail.CandidacyDetailScreen

const val CANDIDACY_DETAIL_ROUTE = "candidacy_detail_route"
private const val ARG_CANDIDACY_ID = "candidacyId"
const val CANDIDACY_DETAIL_ROUTE_WITH_ARG = "$CANDIDACY_DETAIL_ROUTE/{$ARG_CANDIDACY_ID}"

fun NavController.navigateToCandidacyDetail(candidacyId: Int) {
    this.navigate("$CANDIDACY_DETAIL_ROUTE/$candidacyId")
}

fun NavGraphBuilder.candidacyDetailGraph(
    navController: NavController,
) {
    composable(
        route = CANDIDACY_DETAIL_ROUTE_WITH_ARG,
        arguments = listOf(navArgument(ARG_CANDIDACY_ID) { type = NavType.IntType })
    ) { backStackEntry ->
        CandidacyDetailScreen(navController = navController)
    }
}
