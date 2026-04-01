package com.scrumdapp.checkpointservice.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
public class SecurityConfiguration: WebMvcConfigurer {

    @Bean
    public fun filterChain(httpSec: HttpSecurity): SecurityFilterChain {
        httpSec
            .csrf { it.disable() }
            .authorizeHttpRequests(this::createRequestMatcher)


        return httpSec.build()
    }

    private fun createRequestMatcher(auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        auth
            .requestMatchers("/**").permitAll()
    }
}