package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

fun CheckpointSession.toDto(checkpoints: List<Checkpoint>): CheckpointSessionResponseDto {
    return CheckpointSessionResponseDto(
        id = this.id,
        groupId = this.groupId,
        ownerId = this.groupUserId,
        createdDate = this.createdDate,
        startTime = this.startTime,
        endTime = this.startTime.plusMinutes(this.durationMinutes.toLong()),
        checkpoints = checkpoints.map { it.toDto() }
    )
}

fun CheckpointSession.toPartialDto(): CheckpointSessionPartialDto {
    val endTime = this.startTime.plusMinutes(this.durationMinutes.toLong())
    var remainingTime: Long = 0
    if (this.createdDate == LocalDate.now()) {
        remainingTime = Duration.between(LocalTime.now(), endTime).toSeconds()
    }

    return CheckpointSessionPartialDto(
        id = this.id,
        startTime = this.startTime,
        endTime = endTime,
        remainingTime = remainingTime
    )
}

fun CheckpointSessionCreationDto.toEntity(
    groupId: Int,
    ownerId: Int,
): CheckpointSession {
    return CheckpointSession().apply {
        this.groupId = groupId
        this.groupUserId = ownerId
        durationMinutes = this@toEntity.duration?: 15
    }
}