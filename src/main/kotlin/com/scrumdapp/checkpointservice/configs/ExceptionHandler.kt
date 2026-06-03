package com.scrumdapp.checkpointservice.configs

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Component
class ExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<ApiExceptionResponse> {
        return ResponseEntity.status(e.code).body(ApiExceptionResponse(e.code, e.message))
    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintException(e: ConstraintViolationException): ResponseEntity<ApiExceptionResponse> {
        val violations = e.constraintViolations
        val builder = StringBuilder()
        for (violation in violations) {
            val error = "${violation.invalidValue}: ${violation.message}. "
            builder.append(error)
        }
        return ResponseEntity.status(400).body(ApiExceptionResponse(400, builder.toString()))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiExceptionResponse> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { it.defaultMessage ?: "Invalid field" }
        return ResponseEntity.status(400).body(ApiExceptionResponse(400, message))
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

class ForbiddenException(
    override val code: Int = 403,
    override val message: String? = "Forbidden"
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

