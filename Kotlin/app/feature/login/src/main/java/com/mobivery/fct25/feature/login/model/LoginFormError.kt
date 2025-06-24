package com.mobivery.fct25.feature.login.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.login.R

enum class LoginFormError(@StringRes val messageResourceId: Int) {
    EMPTY(R.string.common_mandatory_text),
    INVALID_CREDENTIALS(R.string.login_error_credentials_text)
}
