package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId


fun CheckpointSession.toDto(): CheckpointSessionResponseDto {
    val zonedDateTime = ZonedDateTime.of(this.createdDate, this.startTime, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz")

    return CheckpointSessionResponseDto(
        id = this.id,
        groupId = this.groupId,
        ownerId = this.groupUserId,
        startTime = zonedDateTime.format(formatter),
        name = this.name,
        duration = this.durationMinutes.toLong()
    )
}

fun CheckpointSessionCreationDto.toEntity(
    groupId: Long,
    ownerId: Long,
    name: String?
): CheckpointSession {
    return CheckpointSession().apply {
        this.groupId = groupId
        this.groupUserId = ownerId
        this.name = name
        durationMinutes = this@toEntity.duration ?: 15
    }
}
