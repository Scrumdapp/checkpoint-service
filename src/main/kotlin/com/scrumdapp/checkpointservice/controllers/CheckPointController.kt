package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.services.CheckPointService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/groups/{groupId}/checkpoints")
class CheckpointController(
    private val checkPointService: CheckPointService
) {

    @GetMapping
    fun getCheckpoints(
        @PathVariable groupId: Int,
        @RequestParam(required = false) sessionId: Int?,
    ): List<CheckpointResponseDto> {
        sessionId ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "sessionId is required")
        return checkPointService.findAllBySessionId(sessionId)
    }

    @GetMapping
    fun getCheckpointsByUser(
        @PathVariable groupId: Int,
        @RequestParam(required = false) groupUserId: Int,
        @RequestParam(required = false) sessionId: Int?,
    ): List<CheckpointResponseDto> {
        return checkPointService.findAllByGroupUserId(groupUserId)
    }

    @PatchMapping("/{sessionId}")
    fun updateCheckpoints(
        @PathVariable groupId: Int,
        @PathVariable sessionId: Int,
        @RequestBody dto: List<CheckpointPatchDto>
    ): List<CheckpointResponseDto> {
        return dto.map { checkPointService.upsertCheckpoint(sessionId, groupId, 1, it) }
    }
}
