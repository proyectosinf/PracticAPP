package com.mobivery.fct25.data.network.factory

import com.mobivery.fct25.data.network.model.error.GENERIC_ERROR_CODE
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class ErrorFactoryTest {

    @Test
    fun `buildError should return default ErrorData when exception is null`() {
        val errorData = ErrorFactory.buildError(null)

        assertEquals(GENERIC_ERROR_CODE, errorData.code)
        assertEquals("", errorData.message)
    }

    @Test
    fun `buildError should return default ErrorData when exception is not HttpException`() {
        val errorData = ErrorFactory.buildError(Throwable("Non-HTTP exception"))

        assertEquals(GENERIC_ERROR_CODE, errorData.code)
        assertEquals("", errorData.message)
    }

    @Test
    fun `buildError should return parsed ErrorData when exception is HttpException with valid JSON`() {
        val errorJson = "{\"title\": \"Error title\", \"message\": \"Error message\"}"
        val responseBody = errorJson.toResponseBody()
        val response = Response.error<Any>(400, responseBody)
        val httpException = HttpException(response)

        val errorData = ErrorFactory.buildError(httpException)

        assertEquals("Error message", errorData.message)
    }

    @Test
    fun `buildError should return default ErrorData when exception is HttpException with invalid JSON`() {
        val errorJson = "Invalid JSON"
        val responseBody = errorJson.toResponseBody()
        val response = Response.error<Any>(400, responseBody)
        val httpException = HttpException(response)

        val errorData = ErrorFactory.buildError(httpException)
        assertEquals(GENERIC_ERROR_CODE, errorData.code)
        assertEquals("", errorData.message)
    }

    @Test
    fun `buildError should return default ErrorData when exception is HttpException with null response`() {
        val httpException = mockk<HttpException>()
        every { httpException.response() } returns null

        val errorData = ErrorFactory.buildError(httpException)
        assertEquals(GENERIC_ERROR_CODE, errorData.code)
        assertEquals("", errorData.message)
    }
}