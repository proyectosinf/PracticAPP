package com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.PublishedOffersScreen

const val PUBLISHED_OFFERS_ROUTE = "published_offers_route"

fun NavController.navigateToPublishedOffers() {
    this.navigate(PUBLISHED_OFFERS_ROUTE)
}

fun NavGraphBuilder.publishedOffersGraph(
    navController: NavController,
) {
    composable(PUBLISHED_OFFERS_ROUTE) {
        PublishedOffersScreen(navController = navController)
    }
}
