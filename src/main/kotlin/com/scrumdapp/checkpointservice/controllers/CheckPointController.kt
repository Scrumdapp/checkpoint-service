package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toPartialDto
import com.scrumdapp.checkpointservice.services.CheckPointService
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointController(
    private val sessionService: CheckpointSessionService,
    private val checkPointService: CheckPointService
) {

    @GetMapping("/{sessionId}")
    fun getCheckpoints(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int,
        @RequestParam(required = false) partial: Boolean?
    ): SessionResponseDto {
        return if (partial != null && partial) {
            sessionService.getPartialSession(groupId, sessionId) ?: throw NotFoundException(message =  "Checkpoint with id $sessionId not found")
        } else {
            sessionService.getSession(groupId, sessionId) ?: throw NotFoundException(message =  "Checkpoint with id $sessionId not found")
        }
    }

    @PatchMapping("/{sessionId}")
    fun updateCheckpoint(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int,
        @RequestBody dto: CheckpointPatchDto
    ): CheckpointResponseDto {
        return checkPointService.upsertCheckpoint(sessionId, groupId, 1, dto)
    }
}
