package com.mobivery.fct25.feature.worktutor.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.worktutor.R

enum class OfferFormError(@StringRes val messageResourceId: Int) {
    EMPTY_TITLE(R.string.common_mandatory_text),

    EMPTY_VACANCIES(R.string.offer_mandatory_vacancy_text),
    INVALID_VACANCIES(R.string.offer_example_vacancy_text),
    EMPTY_TYPE(R.string.offer_required_type_text),
    EMPTY_DEGREE(R.string.offer_required_degree_text),
    INVALID_DATES(R.string.offer_invalid_date_text),
    START_DATE_REQUIRED(R.string.offer_select_dates_text),
    START_DATE_AFTER_END_DATE(R.string.offer_error_dates_text),
    END_DATE_REQUIRED(R.string.offer_required_dates_text),
    EMPTY_ADDRESS(R.string.offer_mandatory_address_text),
    EMPTY_POSTAL_CODE(R.string.offer_mandatory_cp_text),
    INVALID_POSTAL_CODE(R.string.offer_invalid_postal_code_text),
    TYPE_REQUIRED(R.string.offer_required_type_text),
    EMPTY_CONTACT_NAME(R.string.offer_mandatory_name_text),
    EMPTY_CONTACT_EMAIL(R.string.offer_example_email_text),
    INVALID_CONTACT_EMAIL(R.string.offer_example_email_text),
    INVALID_CONTACT_PHONE(R.string.offer_phone_format_text),
}
