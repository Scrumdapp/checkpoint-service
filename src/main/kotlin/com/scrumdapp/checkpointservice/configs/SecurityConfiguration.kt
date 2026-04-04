package com.scrumdapp.checkpointservice.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.scrumdapp.checkpointservice.ApiExceptionResponse
import com.scrumdapp.checkpointservice.ForbiddenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
public class SecurityConfiguration(
    private val customAuthEntryPoint: CustomAuthEntryPoint
): WebMvcConfigurer {

    @Bean
    public fun filterChain(httpSec: HttpSecurity): SecurityFilterChain {
        httpSec
            .csrf { it.disable() }
            .authorizeHttpRequests(this::createRequestMatcher)
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthEntryPoint)
            }

        return httpSec.build()
    }

    private fun createRequestMatcher(auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        auth
            .requestMatchers("/groups/{groupId}/sessions/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Remove this for production
    }
}

// Move this to another file
@Component
class CustomAuthEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        ObjectMapper().writeValue(response.writer, ApiExceptionResponse(HttpStatus.FORBIDDEN.value(), authException.message))
    }

}