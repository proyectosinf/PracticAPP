package com.mobivery.fct25.data.error

import com.mobivery.fct25.data.log.DataLogger
import com.mobivery.fct25.data.network.factory.ErrorFactory
import com.mobivery.fct25.domain.model.error.AppError
import retrofit2.HttpException

internal suspend fun <T> errorWrapper(
    onError: suspend (AppError) -> Unit = {},
    job: suspend () -> T,
): T {
    try {
        return job()
    } catch (throwable: Throwable) {
        DataLogger.log(
            tag = "DataError",
            message = throwable.message ?: "Unknown error",
            logType = DataLogger.LogType.ERROR,
            error = throwable,
        )

        val appError: AppError = when (throwable) {
            is HttpException -> {
                val errorData = ErrorFactory.buildError(throwable)
                AppError.ApiError(
                    code = errorData.code,
                    serverMessage = errorData.message
                )
            }

            else -> AppError.InternalError(errorMessage = throwable.message)
        }

        onError(appError)
        throw appError
    }
}
