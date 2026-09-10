package com.scrumdapp.checkpointservice.mappers

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.SessionDateResponseDto
import com.scrumdapp.checkpointservice.dto.SessionDates
import com.scrumdapp.checkpointservice.dto.SessionDatesRaw
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId


fun CheckpointSession.toDto(): SessionResponseDto {
    val zonedDateTime = ZonedDateTime.of(this.createdDate, this.startTime, ZoneId.systemDefault())

    return SessionResponseDto(
        id = this.id,
        groupId = this.groupId,
        ownerId = this.groupUserId,
        startTime = zonedDateTime.format(DateTimeFormatter.ISO_INSTANT),
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

fun List<SessionDatesRaw>.toSessionDateResponse(): SessionDateResponseDto {
    val sessionMap = LinkedHashMap<LocalDate, MutableList<Long>>()
    for (s in this ) {
        sessionMap.getOrPut(s.createdDate) { mutableListOf() }.add(s.id)
    }
    val dates = sessionMap.map { (date, ids) -> SessionDates(date, ids) }

    return SessionDateResponseDto(
        this.minOfOrNull { t -> t.createdDate },
        this.maxOfOrNull { t -> t.createdDate },
        dates
    )
}

fun CheckpointSession.isActive(): Boolean {
    if (this.createdDate != LocalDate.now()) return false
    val endTime = this.startTime.plusMinutes(this.durationMinutes.toLong())
    return !LocalTime.now().isAfter(endTime)
}