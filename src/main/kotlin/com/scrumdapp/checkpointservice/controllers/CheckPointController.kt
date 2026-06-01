package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.services.CheckPointService
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
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
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @RequestParam(required = false) sessionId: Long?,
    ): List<CheckpointResponseDto> {
        val userId = passport.userId.toLong()
        val userGroupId = passport.userGroups?.find { it.toLong() == groupId }?.toLong()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        return when {
            sessionId != null -> checkPointService.findAllBySessionIdAndGroupUserId(sessionId, userId)
            else -> checkPointService.findAllByGroupUserId(userId)
        }
    }

    @GetMapping("/{sessionId}")
    fun getCheckpointBySession(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @PathVariable sessionId: Long
    ): List<CheckpointResponseDto> {
        val userId = passport.userId.toLong()
        val userGroupId = passport.userGroups?.find { it.toLong() == groupId }?.toLong()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        return checkPointService.findAllBySessionId(sessionId)
    }




    @PatchMapping("/{sessionId}")
    fun updateCheckpoints(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @PathVariable sessionId: Long,
        @RequestBody dto: List<CheckpointPatchDto>
    ): List<CheckpointResponseDto> {
        val userId = passport.userId.toLong()
        val userGroupId = passport.userGroups?.find { it.toLong() == groupId }?.toLong()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of this group")

        return dto.map { checkPointService.upsertCheckpoint(sessionId, userGroupId, userId, it) }
    }
}