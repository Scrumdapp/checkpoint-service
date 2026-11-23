package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.services.CheckPointService
import com.scrumdapp.passportplugin.annotations.groupAccess.IsInGroup
import com.scrumdapp.passportplugin.annotations.passportController.Passport
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
    @IsInGroup
    fun getCheckpoints(
        @PathVariable groupId: Long,
        @RequestParam(required = false) session: Long?,
        ): List<CheckpointResponseDto> {

        if (session == null) throw BadRequestException(message = "Param session is expected")

        return checkPointService.findAllBySessionId(session, groupId)
    }

    @PatchMapping
    @IsInGroup
    fun patchCheckpoint(
        @Passport passport: PassportContent,
        @PathVariable groupId: Long,
        @Valid @RequestBody checkpoint: CheckpointPatchDto,

    ): CheckpointResponseDto {
        return checkPointService.upsertCheckpoint(groupId, checkpoint, passport.userId)
    }
}