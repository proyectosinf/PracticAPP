package com.mobivery.fct25.feature.components.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.components.R

enum class ComponentFormError(@StringRes val messageResourceId: Int) {
    FORMAT(R.string.common_error_text),
}
