package com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation.navigateToPublishedOffers
import com.mobivery.fct25.feature.worktutor.ui.worktutor.createoffer.CreateOfferScreen

const val CREATE_OFFER_ROUTE = "create_offer_route"

fun NavController.navigateToCreateOffer() {
    this.navigate(CREATE_OFFER_ROUTE)
}

fun NavGraphBuilder.createOfferGraph(
    navController: NavController,
) {
    composable(CREATE_OFFER_ROUTE) {
        CreateOfferScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToPublishedOffers = { navController.navigateToPublishedOffers() }
        )
    }
}
