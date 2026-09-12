package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.services.CheckPointService
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/groups/{groupId}/checkpoints")
class CheckpointController(
    private val checkPointService: CheckPointService
) {

    @GetMapping
    fun getCheckpoints(
        @PathVariable groupId: Int,
        @RequestParam(required = false) session: Long,
        ): List<CheckpointResponseDto> {
        return checkPointService.findAllBySession(session)
    }

    @PatchMapping
    fun patchCheckpoint(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @Valid @RequestBody checkpoint: CheckpointPatchDto,
    ): CheckpointResponseDto {
        return checkPointService.upsert(groupId, checkpoint, passport.userId.toLong())
    }
}