package com.scrumdapp.checkpointservice.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.filters.usePassport
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.http.HttpMethod
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
public class SecurityConfiguration(
    private val customAuthEntryPoint: CustomAuthEntryPoint,
    private val passportAuthFilter: PassportAuthFilter
): WebMvcConfigurer {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .usePassport(passportAuthFilter)
            .authorizeHttpRequests {
                it.requestMatchers("/groups/{groupId}/sessions/**").hasAnyAuthority("STUDENT", "COACH")
                it.requestMatchers("/groups/{groupId}/checkpoints/**").hasAnyAuthority("STUDENT", "COACH")
                it.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthEntryPoint)
                it.accessDeniedHandler { _, response, _ ->
                    response.status = HttpServletResponse.SC_FORBIDDEN
                }
            }

        return http.build()
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
        if (response.isCommitted) return
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.writer, ApiExceptionResponse(HttpStatus.UNAUTHORIZED.value(), authException.message))
    }
}

