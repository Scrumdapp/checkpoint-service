package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.configs.BadRequestException
import com.scrumdapp.checkpointservice.configs.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointSessionController(
    private val sessionService: CheckpointSessionService
) {

    @GetMapping
    fun getSessionsBetweenDates(
        @PathVariable groupId: Int,
        @RequestParam(required = false) onlyActive: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate?
    ): List<CheckpointSessionResponseDto> {
        if (from != null && to != null && from.isAfter(to)) {
            throw BadRequestException(message = "from date must be before to date")
        }

        return when {
            onlyActive == true -> sessionService.getActiveSessions(groupId, date)
            from != null && to != null -> sessionService.getSessionsBetweenDates(groupId, from, to)
            else -> sessionService.getSessions(groupId, date)
        }
    }



    @GetMapping("/on-date")
    fun getSessionsOnDate(
        @PathVariable groupId: Int,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): List<CheckpointSessionResponseDto> {
        return sessionService.getSessionOnDate(groupId, date)
    }
    @GetMapping("/{sessionId}")
    fun getSession(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int
    ): CheckpointSessionResponseDto {
        return sessionService.getSession(groupId, sessionId)
            ?: throw NotFoundException(message = "session with $sessionId not found")
    }

    @PostMapping
    fun createSession(
        res: HttpServletResponse,
        //To Do: Add interceptor for header information here
        @PathVariable groupId: Int,
        @Valid @RequestBody dto: CheckpointSessionCreationDto
    ): CheckpointSessionResponseDto {
        res.status = HttpStatus.CREATED.value()
        return sessionService.createSession(groupId, 1, dto)
    }
}