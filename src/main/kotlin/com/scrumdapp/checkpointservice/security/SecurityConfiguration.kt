package com.scrumdapp.checkpointservice.security

import com.scrumdapp.checkpointservice.errors.CustomAccessDeniedHandler
import com.scrumdapp.checkpointservice.errors.CustomAuthEntrypointHandler
import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.filters.usePassport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
public class SecurityConfiguration(
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
            .exceptionHandling { ex -> ex
                .authenticationEntryPoint(CustomAuthEntrypointHandler())
                .accessDeniedHandler(CustomAccessDeniedHandler())
            }

        return http.build()
    }
}

