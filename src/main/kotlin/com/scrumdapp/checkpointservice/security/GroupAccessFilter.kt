package com.scrumdapp.checkpointservice.security

import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.passportplugin.jwt.PassportService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class GroupAccessFilter(
    val passportService: PassportService
): OncePerRequestFilter(
) {
    private val groupPaths = Regex("""^/groups/([^/]+)$""")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val group = groupPaths.matchEntire(request.requestURI)?.groupValues?.getOrNull(1)

        if (group != null) {
            val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
                ?: throw ForbiddenException()
            val userGroups = passportService.extractUserGroups(jwt)

            if (!userGroups.contains(group.toInt())) {
                throw ForbiddenException(message = "User is not a member of this group")
            }
        }

        filterChain.doFilter(request, response)
    }
}