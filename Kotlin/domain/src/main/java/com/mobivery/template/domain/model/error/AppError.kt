package com.mobivery.fct25.domain.model.error

sealed class AppError : Throwable() {
    data class InternalError(
        val errorMessage: String?
    ) : AppError()

    data class ApiError(
        val code: Int?,
        val serverMessage: String?,
    ) : AppError()

    data class Unknown(
        val errorMessage: String?
    ) : AppError()

    data class Login(
        val messageResId: Int
    ) : AppError()

    data class UnauthorizedError(
        val reason: String? = null
    ) : AppError()
}
