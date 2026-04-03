package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession

fun Checkpoint.toDto(): CheckpointResponseDto {
    return CheckpointResponseDto(
        id = this.id,
        groupUserId = this.groupUserId,
        presence = this.presence,
        impediment = this.impediment,
        stars = this.stars,
        comment = this.comment,
    )
}

fun CheckpointPatchDto.toEntity(
    session: CheckpointSession
): Checkpoint {
    return Checkpoint().apply {
        groupUserId = this.groupUserId
        presence = this.presence
        impediment = this.impediment
        stars = this.stars
        comment = this.comment
        checkpointSession = session
        checkpointSessionId = session.id
    }
}