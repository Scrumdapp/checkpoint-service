package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toPartialDto
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/sessions")
class CheckpointSessionController(
    private val sessionService: CheckpointSessionService
) {

    @GetMapping("/{groupId}")
    fun getSessions(
        @PathVariable groupId: Int,
        @RequestParam(required = false) onlyActive: Boolean?
    ): List<CheckpointSessionResponseDto> {

        return if (onlyActive != null && onlyActive) {
            sessionService.getActiveSessions(groupId).map { it.toDto() }
        } else {
            sessionService.getSessions(groupId).map { it.toDto() }
        }
    }

    @PostMapping("/{groupId}")
    fun createSession(
        //To Do: Add interceptor for header information here
        @PathVariable groupId: Int,
        @RequestBody dto: CheckpointSessionCreationDto
    ): CheckpointSessionResponseDto {
        return sessionService.createSession(groupId, 1, dto).toDto()
    }


    @GetMapping("/{groupId}/{sessionId}")
    fun getSession(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int,
        @RequestParam(required = false) partial: Boolean?
    ): SessionResponseDto {
        val session = sessionService.getSession(groupId, sessionId) ?: //will modify this with an actual error
        return CheckpointSessionPartialDto(
            id = 5,
            startTime = LocalTime.now(),
            endTime = LocalTime.now(),
            remainingTime = 69
        )

        return if (partial != null && partial) {
            session.toPartialDto()
        } else {
            session.toDto()
        }
    }
}