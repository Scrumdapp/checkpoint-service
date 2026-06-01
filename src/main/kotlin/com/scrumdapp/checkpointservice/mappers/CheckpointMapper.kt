package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.dto.Presence
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession

fun Checkpoint.toDto(): CheckpointResponseDto {
    return CheckpointResponseDto(
        id = this.id,
        sessionId = this.checkpointSession.id,
<<<<<<< Updated upstream
        groupUser = this.groupUserId,
        presence = this.presence,
=======
        groupUserId = this.groupUserId,
        presence = this.presence?.let { Presence.fromCode(it) },
>>>>>>> Stashed changes
        impediment = this.impediment,
        stars = this.stars,
        comment = this.comment
    )
}

fun Checkpoint.applyPatch(dto: CheckpointPatchDto) = apply {
    dto.presence?.let { presence = it.code }
    dto.impediment?.let { impediment = it.trim() }
    dto.stars?.let { stars = it }
    dto.comment?.let { comment = it.trim() }
}

fun CheckpointPatchDto.toEntity(
    session: CheckpointSession,
    userId: Long
): Checkpoint {
    return Checkpoint(session).apply {
        groupUserId = userId
        presence = this@toEntity.presence?.code
        impediment = this@toEntity.impediment
        stars = this@toEntity.stars
        comment = this@toEntity.comment
    }
}