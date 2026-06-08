package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.errors.ServerFaultException
import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController
@Validated
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointSessionController(
    private val sessionService: CheckpointSessionService
) {

    @GetMapping
    fun getSessionsBetweenDates(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @RequestParam(required = false) onlyActive: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): List<CheckpointSessionResponseDto> {
        passport.userGroups?.find { it.toLong() == groupId }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        if (from != null && to != null && from.isAfter(to)) {
            throw BadRequestException(message = "from date must be before to date")
        }

        return when {
            onlyActive == true -> sessionService.getActiveSessions(groupId, date)
            from != null && to != null -> sessionService.getSessionsBetweenDates(groupId, from, to)
            else -> sessionService.getSessions(groupId, date)
        }
    }


    @GetMapping("/{sessionId}")
    fun getSession(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @PathVariable sessionId: Long
    ): CheckpointSessionResponseDto {
        passport.userGroups?.find { it.toLong() == groupId }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        return sessionService.getSession(groupId, sessionId)
            ?: throw NotFoundException(message = "session with $sessionId not found")
    }

    @PostMapping
    fun createSession(
        @Passport passport: PassportContent,
        res: HttpServletResponse,
        @PathVariable groupId: Long,
        @Valid @RequestBody dto: CheckpointSessionCreationDto
    ): CheckpointSessionResponseDto {
        passport.userGroups?.find { it.toLong() == groupId }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt ?: throw ServerFaultException()

        res.status = HttpStatus.CREATED.value()
        return sessionService.createSession(jwt, groupId, passport.userId.toLong(), dto)
    }
}