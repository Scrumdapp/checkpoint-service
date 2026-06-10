package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.errors.BadRequestException
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
        @Passport passport: PassportContent,
        @PathVariable groupId: Int,
        @RequestParam(required = false) session: Long?,
        ): List<CheckpointResponseDto> {

        if (passport.userGroups.isNullOrEmpty() || passport.userGroups?.contains(groupId) == false ) throw ForbiddenException(message = "Forbidden, user not part of group")
        if (session == null) throw BadRequestException(message = "Param session is expected")

        return checkPointService.findAllBySessionId(session, groupId.toLong())
    }

    @PatchMapping
    fun patchCheckpoint(
        @Passport passport: PassportContent,
        @PathVariable groupId: Int,
        @Valid @RequestBody checkpoint: CheckpointPatchDto,

    ): CheckpointResponseDto {
        if (passport.userGroups.isNullOrEmpty() || passport.userGroups?.contains(groupId) == false ) throw ForbiddenException(message = "User is not a member of this group")
        return checkPointService.upsertCheckpoint(groupId.toLong(), checkpoint, passport.userId.toLong())
    }
}