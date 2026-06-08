package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionResponseDto(
    val id: Long,
    val startTime: String,
    val duration: Long,
    val groupId: Long,
    val ownerId: Long,
    val name: String?,
)

data class CheckpointSessionCreationDto(
    @JsonProperty("duration")
    val duration: Int? = null,

    @field:Size(max = 32, message = "Name cannot exceed 32 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9 \\-]*$", message = "Name can only contain letters, numbers, spaces and hyphens")
    val name: String? = null
)