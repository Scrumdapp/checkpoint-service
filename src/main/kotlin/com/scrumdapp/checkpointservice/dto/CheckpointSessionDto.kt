package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalTime

sealed interface SessionResponseDto {
    val id: Long
    val startTime: String
    val duration: Long
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionResponseDto(
    override val id: Long,
    override val startTime: String,
    override val duration: Long,
    val groupId: Long,
    val ownerId: Long,
    val date: LocalDate,
    val name: String?,
): SessionResponseDto

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionPartialDto(
    override val id: Long,
    override val startTime: String,
    override val duration: Long,
    val remainingTime: Long,
): SessionResponseDto



data class CheckpointSessionCreationDto(
    @JsonProperty("duration")
    val duration: Int? = null,

    @field:Size(max = 40, message = "Name cannot exceed 40 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Name can only contain letters, numbers and spaces")
    val name: String? = null
)