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

fun Checkpoint.applyPatch(dto: CheckpointPatchDto) = apply {
    dto.presence?.let { presence = it}
    dto.impediment?.let { impediment = it.trim() }
    dto.stars?.let { stars = it }
    dto.comment?.let { comment = it.trim() }
}

fun CheckpointPatchDto.toEntity(
    session: CheckpointSession,
    userId: Int
): Checkpoint {
    return Checkpoint().apply {
        groupUserId = userId
        presence = this@toEntity.presence
        impediment = this@toEntity.impediment
        stars = this@toEntity.stars
        comment = this@toEntity.comment
        checkpointSession = session
    }
}