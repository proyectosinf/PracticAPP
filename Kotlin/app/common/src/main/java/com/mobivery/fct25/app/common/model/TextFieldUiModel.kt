package com.mobivery.fct25.app.common.model

data class TextFieldUiModel<T>(
    val text: String,
    val errorType: T? = null,
)