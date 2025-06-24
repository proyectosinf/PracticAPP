package com.mobivery.fct25.feature.register.model

import androidx.annotation.StringRes
import com.mobivery.fct25.feature.register.R

enum class RegisterGlobalError(@StringRes val messageRes: Int) {
    FIREBASE_USER_NULL(R.string.register_error_firebase_user_null_text),
    FIREBASE_TOKEN_ERROR(R.string.register_error_firebase_token_text),
    BACKEND_DNI_ALREADY_EXISTS(R.string.register_error_dni_already_exists_text),
    BACKEND_ERROR(R.string.register_error_backend_generic_text),
    FIREBASE_ERROR(R.string.register_error_firebase_generic_text),
    BACKEND_SOCIAL_SECURITY_NUMBER_ALREADY_EXISTS(R.string.register_error_social_security_already_exists_text),
}