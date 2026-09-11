package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.dto.Presence
import com.scrumdapp.checkpointservice.entities.Checkpoint

fun Checkpoint.toDto(): CheckpointResponseDto {
    return CheckpointResponseDto(
        id = this.id,
        sessionId = this.checkpointSession.id,
        groupUser = this.groupUserId,
        presence = this.presence?.let { Presence.fromCode(it) },
        impediment = this.impediment,
        stars = this.stars,
        comment = this.comment
    )
}

fun Checkpoint.applyPatch(dto: CheckpointPatchDto): Checkpoint {

    val entityPresence = Presence.fromString(dto.presence)

    return apply {
        dto.presence?.let { presence = entityPresence?.code}
        dto.impediment?.let { impediment = it.trim() }
        dto.stars?.let { stars = it }
        dto.comment?.let { comment = it.trim() }
    }
}