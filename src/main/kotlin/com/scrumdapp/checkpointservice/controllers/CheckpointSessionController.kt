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
import org.springframework.format.annotation.DateTimeFormat
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
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointSessionController(
    private val sessionService: CheckpointSessionService
) {

    @GetMapping
    fun getSessions(
        @PathVariable groupId: Int,
        @RequestParam(required = false) onlyActive: Boolean?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate?
    ): List<CheckpointSessionResponseDto> {
        return if (onlyActive != null && onlyActive) {
            sessionService.getActiveSessions(groupId, date)
        } else {
            sessionService.getSessions(groupId, date)
        }
    }

    @PostMapping
    fun createSession(
        //To Do: Add interceptor for header information here
        @PathVariable groupId: Int,
        @RequestBody dto: CheckpointSessionCreationDto
    ): CheckpointSessionResponseDto {
        return sessionService.createSession(groupId, 1, dto)
    }
}