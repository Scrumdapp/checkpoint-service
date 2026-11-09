package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.services.CheckpointNameService
import com.scrumdapp.passportplugin.annotations.groupAccess.IsInGroup
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
    private val checkPointNamesService: CheckpointNameService
) {

    @GetMapping
    @IsInGroup
    fun getCheckpointNames(
        @PathVariable groupId: Long,
    ): CheckpointNamesDto {
        return checkPointNamesService.getCheckpointNames(groupId)
    }

    @PutMapping
    @IsInGroup
    fun putCheckpointName(
        @Valid @RequestBody dto: CheckpointNamesDto,
        @PathVariable groupId: Long,
    ): CheckpointNamesDto {
        return checkPointNamesService.updateCheckPointNames(groupId, dto)
    }
}