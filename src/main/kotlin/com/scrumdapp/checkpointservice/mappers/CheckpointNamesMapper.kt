package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.entities.CheckpointNames


fun CheckpointNames.toDto(): CheckpointNamesDto {
    return CheckpointNamesDto(this.checkpoint_names)
}

fun CheckpointNames.patchFromDto(dto: CheckpointNamesDto): CheckpointNames {
    return this.apply {
        checkpoint_names = dto.checkpointNames
    }
}