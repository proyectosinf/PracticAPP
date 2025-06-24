package com.mobivery.fct25.feature.worktutor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation.PUBLISHED_OFFERS_ROUTE
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation.publishedOffersGraph
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.navigation.createOfferGraph

const val WORKTUTOR_NESTED_NAVIGATION_ROUTE = "worktutor_nested_navigation_route"

fun NavController.navigateToWorktutor() {
    this.navigate(WORKTUTOR_NESTED_NAVIGATION_ROUTE)
}

fun NavGraphBuilder.worktutorFeatureGraph(
    navController: NavController,
) {
    navigation(
        startDestination = PUBLISHED_OFFERS_ROUTE,
        route = WORKTUTOR_NESTED_NAVIGATION_ROUTE
    ) {
        publishedOffersGraph(navController)
        createOfferGraph(navController)
    }
}
