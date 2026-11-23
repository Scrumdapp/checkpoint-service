package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.errors.ServerFaultException
import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.SessionDateResponseDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import com.scrumdapp.passportplugin.annotations.groupAccess.IsInGroup
import com.scrumdapp.passportplugin.annotations.passportController.Passport
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
import java.time.LocalDate
import java.time.YearMonth

@RestController
@Validated
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointSessionController(
    private val sessionService: CheckpointSessionService
) {

    @GetMapping
    @IsInGroup
    fun getSessionsBetweenDates(
        @PathVariable groupId: Long,
        @RequestParam(required = false) onlyActive: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): List<SessionResponseDto> {


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
    @IsInGroup
    fun getSession(
        @PathVariable groupId: Long,
        @PathVariable sessionId: Long
    ): SessionResponseDto {
        return sessionService.getSession(groupId, sessionId)
            ?: throw NotFoundException(message = "session with $sessionId not found")
    }

    @GetMapping("/dates")
    @IsInGroup
    fun getRecentSessions(
        @PathVariable groupId: Long,
        @RequestParam(required = false) limit: Int?,
        @RequestParam(required = false) month: YearMonth?,
    ): SessionDateResponseDto {

        if (limit != null && limit !in 1..20) throw BadRequestException(message = "Limit must be between 0 and 20")
        if (month != null) {
            return sessionService.getRecentCalendarSessions(groupId, month, limit ?: 31)
        }
        return sessionService.getRecentSessions(groupId, limit ?: 5)
    }


    @GetMapping("/months")
    @IsInGroup
    fun getMonthsWithSessions(
        @PathVariable groupId: Long
    ): List<String> {
        return sessionService.getMonthsWithSessions(groupId)
    }

    @PostMapping
    @IsInGroup
    fun createSession(
        @Passport passport: PassportContent,
        res: HttpServletResponse,
        @PathVariable groupId: Long,
        @Valid @RequestBody dto: CheckpointSessionCreationDto
    ): SessionResponseDto {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt ?: throw ServerFaultException()

        res.status = HttpStatus.CREATED.value()
        return sessionService.createSession(jwt, groupId, passport.userId, dto)
    }
}