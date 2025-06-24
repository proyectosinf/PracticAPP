package com.mobivery.fct25.data.network.model.error

const val GENERIC_ERROR_CODE = -1

internal data class ErrorData(
    val code: Int? = GENERIC_ERROR_CODE,
    val message: String? = "",
)