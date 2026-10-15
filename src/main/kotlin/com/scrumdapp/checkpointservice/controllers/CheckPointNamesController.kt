package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.services.CheckPointNamesService
import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/groups/{groupId}/checkpoints/names")
class CheckpointNamesController(
    private val checkPointNamesService: CheckPointNamesService
) {

    @GetMapping
    fun getCheckpointNames(
        @Passport passport: PassportContent,
        @PathVariable groupId: Int,
    ): CheckpointNamesDto {
        if (passport.userGroups.isNullOrEmpty() || passport.userGroups?.contains(groupId) == false ) throw ForbiddenException(message = "Forbidden, user not part of group")
        return checkPointNamesService.getCheckPointNames(groupId)
    }

    @PutMapping
    fun putCheckpointName(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: CheckpointNamesDto,
        @PathVariable groupId: Int,
    ): CheckpointNamesDto {
        if (passport.userGroups.isNullOrEmpty() || passport.userGroups?.contains(groupId) == false ) throw ForbiddenException(message = "Forbidden, user not part of group")
        return checkPointNamesService.updateCheckPointNames(groupId, dto)
    }
}