package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toPartialDto
import com.scrumdapp.checkpointservice.services.CheckpointSessionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalTime

@RestController
@RequestMapping("/groups/{groupId}/sessions")
class CheckpointController(
    private val sessionService: CheckpointSessionService
) {
    // Checkpoint voor 1 dag

    @GetMapping("/{sessionId}")
    fun getCheckpoints(
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

    @PatchMapping("/{sessionId}")
    fun updateCheckpoint(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int,
    ): CheckpointResponseDto {

    }
}