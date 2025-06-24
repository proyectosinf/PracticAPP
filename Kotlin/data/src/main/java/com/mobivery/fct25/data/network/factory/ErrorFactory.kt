package com.mobivery.fct25.data.network.factory

import com.google.gson.Gson
import com.mobivery.fct25.data.network.model.error.ErrorData
import retrofit2.HttpException
import retrofit2.Response

internal class ErrorFactory {

    companion object {
        fun buildError(exception: Throwable?) = if (exception is HttpException) {
            getRequestError(exception.response())
        } else {
            ErrorData()
        }

        private fun getRequestError(responseBody: Response<*>?): ErrorData {
            return responseBody?.errorBody()?.let {errorBody ->
                try {
                    Gson().fromJson(errorBody.charStream(), ErrorData::class.java)
                } catch (e: Throwable) {
                    ErrorData()
                }
            }?: ErrorData()
        }
    }
}