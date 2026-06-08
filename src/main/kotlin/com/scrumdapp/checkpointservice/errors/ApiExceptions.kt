package com.scrumdapp.checkpointservice.errors

import com.fasterxml.jackson.annotation.JsonInclude

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
    override val message: String? = "Internal Server Error",
    override val enableLogging: Boolean = true
): ApiException(code, message, enableLogging)