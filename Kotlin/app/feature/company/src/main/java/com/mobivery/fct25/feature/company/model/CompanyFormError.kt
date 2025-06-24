package com.mobivery.fct25.feature.company.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.company.R

enum class CompanyFormError(@StringRes val messageResId: Int) {
    EMPTY(R.string.company_mandatory_fields_text),
    INVALID_CIF(R.string.company_cif_valid_format_text),
    INVALID_WEB(R.string.company_url_not_valid_text)
}
