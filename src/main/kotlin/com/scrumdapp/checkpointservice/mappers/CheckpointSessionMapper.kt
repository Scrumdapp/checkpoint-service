package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
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

fun CheckpointSession.toPartialDto(): CheckpointSessionPartialDto {
    val zonedDateTime = ZonedDateTime.of(this.createdDate, this.startTime, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz")
    val endTime = this.startTime.plusMinutes(this.durationMinutes.toLong())
    var remainingTime: Long = 0
    if (this.createdDate == LocalDate.now()) {
        remainingTime = Duration.between(LocalTime.now(), endTime).toSeconds()
    }

    return CheckpointSessionPartialDto(
        id = this.id,
        startTime = zonedDateTime.format(formatter),
        duration = this.durationMinutes.toLong(),
        remainingTime = remainingTime
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
