package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

sealed interface SessionResponseDto {
    val id: Int
    val startTime: LocalTime
    val endTime: LocalTime
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionResponseDto(
    override val id: Int,
    override val startTime: LocalTime,
    override val endTime: LocalTime,
    val groupId: Int,
    val ownerId: Int,
    val createdDate: LocalDate,
): SessionResponseDto

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionPartialDto(
    override val id: Int,
    override val startTime: LocalTime,
    override val endTime: LocalTime,
    val remainingTime: Long,
): SessionResponseDto



data class CheckpointSessionCreationDto(

    @JsonProperty("duration")
    val duration: Int? = null, // Standard 15 minutes for the time being
)