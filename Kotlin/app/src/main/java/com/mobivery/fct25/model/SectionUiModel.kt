package com.mobivery.fct25.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mobivery.fct25.R
import com.mobivery.fct25.feature.about.navigation.ABOUT_SECTION_ROUTE
import com.mobivery.fct25.feature.company.ui.viewCompany.navigation.VIEW_COMPANY_ROUTE
import com.mobivery.fct25.feature.components.navigation.COMPONENTS_FEATURE_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.home.navigation.HOME_FEATURE_NAVIGATION_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentcandidacies.navigation.STUDENT_CANDIDACIES_ROUTE
import com.mobivery.fct25.feature.student.ui.student.studentpublishedoffer.navigation.STUDENT_PUBLISHED_OFFERS_ROUTE
import com.mobivery.fct25.feature.worktutor.ui.worktutor.publishedoffers.navigation.PUBLISHED_OFFERS_ROUTE

sealed class SectionUiModel(
    val route: String,
    @StringRes val labelResourceId: Int,
    @DrawableRes val iconResourceId: Int,
    var restoreState: Boolean = false,
) {
    data object Home : SectionUiModel(
        route = HOME_FEATURE_NAVIGATION_ROUTE,
        labelResourceId = R.string.home_title_text, // TODO: change it
        iconResourceId = R.drawable.logo_icon // TODO: change it
    )

    data object About : SectionUiModel(
        route = ABOUT_SECTION_ROUTE,
        labelResourceId = R.string.about_title_text, // TODO: change it
        iconResourceId = R.drawable.logo_icon // TODO: change it
    )

    data object Components : SectionUiModel(
        route = COMPONENTS_FEATURE_NAVIGATION_ROUTE,
        labelResourceId = R.string.components_sample_title_text,
        iconResourceId = R.drawable.logo_icon
    )

    object Company : SectionUiModel(
        route = VIEW_COMPANY_ROUTE,
        labelResourceId = R.string.common_menu_company_label_text,
        iconResourceId = R.drawable.ic_build
    )

    object Offers : SectionUiModel(
        route = PUBLISHED_OFFERS_ROUTE,
        labelResourceId = R.string.common_menu_job_offers_label_text,
        iconResourceId = R.drawable.ic_offer
    )

    object StudentOffers : SectionUiModel(
        route = STUDENT_PUBLISHED_OFFERS_ROUTE,
        labelResourceId = R.string.common_menu_job_offers_label_text,
        iconResourceId = R.drawable.ic_offer
    )

    object Candidacies : SectionUiModel(
        route = STUDENT_CANDIDACIES_ROUTE,
        labelResourceId = R.string.common_menu_candidacies_label_text,
        iconResourceId = R.drawable.ic_person_2
    )
}
