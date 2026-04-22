package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

sealed interface SessionResponseDto {
    val id: Int
    val startTime: LocalTime
    val duration: Long
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionResponseDto(
    override val id: Int,
    override val startTime: LocalTime,
    override val duration: Long,
    val groupId: Int,
    val name: String?,
    val ownerId: Int,
    val createdDate: LocalDate,
    val checkpoints: List<CheckpointResponseDto>,
): SessionResponseDto

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionPartialDto(
    override val id: Int,
    override val startTime: LocalTime,
    override val duration: Long,
    val remainingTime: Long,
): SessionResponseDto



data class CheckpointSessionCreationDto(

    @JsonProperty("duration")
    val duration: Int? = null, // Standard 15 minutes for the time being
    val name: String? = null // Standard Checkpoint Session
)