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
        @PathVariable groupId: Long,
        @RequestParam(required = false) sessionId: Long?,
        @RequestParam(required = false) groupUserId: Long?,
    ): List<CheckpointResponseDto> {

        return when {sessionId != null && groupUserId != null -> checkPointService.findAllBySessionIdAndGroupUserId(sessionId, groupUserId)
            sessionId != null -> checkPointService.findAllBySessionId(sessionId)
            groupUserId != null -> checkPointService.findAllByGroupUserId(groupUserId)
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one parameter is required")
        }
    }

    @PatchMapping("/{sessionId}")
    fun updateCheckpoints(
        @PathVariable groupId: Long,
        @PathVariable sessionId: Long,
        @RequestBody dto: List<CheckpointPatchDto>
    ): List<CheckpointResponseDto> {
        return dto.map { checkPointService.upsertCheckpoint(sessionId, groupId, 1, it) }
    }
}
