package com.scrumdapp.checkpointservice.errors

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Component
class GlobalExceptionHandler {
    val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<ApiExceptionResponse> {
        if (e.enableLogging) logger.error(e.message, e.stackTrace)
        return ResponseEntity.status(e.code).body(ApiExceptionResponse(e.code, e.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiExceptionResponse> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { it.defaultMessage ?: "Invalid field" }
        return ResponseEntity.status(400).body(ApiExceptionResponse(400, message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiExceptionResponse> {
        logger.error(e.message, e.stackTrace)
        return ResponseEntity.status(500).body(ApiExceptionResponse(500, "Something went wrong"))
    }
}

