package com.mobivery.fct25.feature.register.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.register.R

enum class RegisterFormError(@StringRes val messageResourceId: Int) {
    EMPTY(R.string.common_mandatory_text),
    INVALID_EMAIL(R.string.register_invalid_email_text),
    INVALID_PASSWORD(R.string.register_invalid_password_text),
    PASSWORDS_NOT_MATCH(R.string.register_passwords_not_match_text),
    INVALID_DNI(R.string.register_invalid_dni_text),
    INVALID_SOCIAL_SECURITY(R.string.register_invalid_social_security_text),
    NAME_REQUIRED(R.string.register_name_required_text),
    LASTNAME_REQUIRED(R.string.register_lastname_required_text),
    ROLE_REQUIRED(R.string.register_role_required_text),
    MAX_LENGTH_EXCEEDED(R.string.register_max_length_name_surname_text),
    INVALID_PHONE(R.string.register_invalid_phone_text),
}
