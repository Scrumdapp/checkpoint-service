package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SessionResponseDto(
    val id: Long,
    val startTime: String,
    val duration: Long,
    val groupId: Long,
    val ownerId: Long,
    val name: String?,
)

data class SessionDatesRaw(
    val createdDate: LocalDate,
    val id: Long,
)

data class SessionDates(
    val date: LocalDate,
    val sessions: List<Long>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SessionDateResponseDto(
    val fromDate: LocalDate?,
    val toDate: LocalDate?,
    val dates: List<SessionDates>
)

data class CheckpointSessionCreationDto(
    @JsonProperty("duration")
    val duration: Int? = null,

    @field:Size(max = 32, message = "Name cannot exceed 32 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9 \\-]*$", message = "Name can only contain letters, numbers, spaces and hyphens")
    val name: String? = null
)