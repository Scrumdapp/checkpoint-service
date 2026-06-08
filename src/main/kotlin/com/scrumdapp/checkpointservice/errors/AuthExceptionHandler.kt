package com.scrumdapp.checkpointservice.errors

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.FORBIDDEN.value()
        ObjectMapper().writeValue(response.writer,
            ApiExceptionResponse(HttpStatus.FORBIDDEN.value(), accessDeniedException.message)
        )
    }
}

@Component
class CustomAuthEntrypointHandler(): AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        ObjectMapper().writeValue(response.writer,
            ApiExceptionResponse(HttpStatus.UNAUTHORIZED.value(), authException.message)
        )
    }
}