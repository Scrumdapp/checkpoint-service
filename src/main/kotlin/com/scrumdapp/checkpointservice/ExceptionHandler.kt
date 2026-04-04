package com.scrumdapp.checkpointservice

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<ApiExceptionResponse> {
        // To Do add logging & stacktrace
        return ResponseEntity.status(e.code).body(ApiExceptionResponse(e.code, e.message))
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiExceptionResponse(
    val code: Int,
    val message: String?,
    val stackTrace: String? = null
)

open class ApiException(
    open val code: Int,
    override val message: String?,
    open val enableLogging: Boolean = false
): RuntimeException()

class NotFoundException(
    override val code: Int = 404,
    override val message: String? = "Not found"
): ApiException(code, message)

class BadRequestException(
    override val code: Int = 400,
    override val message: String? = "Bad Request"
): ApiException(code, message)

class ServerFaultException(
    override val code: Int = 500,
    override val message: String? = "Bad Request",
    override val enableLogging: Boolean = true
): ApiException(code, message, enableLogging)