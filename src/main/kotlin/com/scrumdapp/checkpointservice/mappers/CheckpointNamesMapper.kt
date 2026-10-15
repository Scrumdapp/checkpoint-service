package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.entities.CheckpointNames


object CheckpointNamesMapper {

    fun toDto(entity: CheckpointNames): CheckpointNamesDto =
        CheckpointNamesDto(
            checkpointNames = entity.checkpoint_names
        )

    fun updateEntity(
        entity: CheckpointNames,
        dto: CheckpointNamesDto
    ) {
        entity.checkpoint_names = dto.checkpointNames
    }
}