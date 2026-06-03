package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun CheckpointSession.toDto(): CheckpointSessionResponseDto {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    return CheckpointSessionResponseDto(
        id = this.id,
        groupId = this.groupId,
        ownerId = this.groupUserId,
        date = this.createdDate,
        startTime = this.startTime.format(formatter),
        name = this.name,
        duration = this.durationMinutes.toLong()
    )
}

fun CheckpointSession.toPartialDto(): CheckpointSessionPartialDto {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val endTime = this.startTime.plusMinutes(this.durationMinutes.toLong())
    var remainingTime: Long = 0
    if (this.createdDate == LocalDate.now()) {
        remainingTime = Duration.between(LocalTime.now(), endTime).toSeconds()
    }

    return CheckpointSessionPartialDto(
        id = this.id,
        startTime = this.startTime.format(formatter),
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