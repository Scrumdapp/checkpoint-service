package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointResponseDto(
    val id: Long,
    val sessionId: Long,
    val groupUser: Long,
    val presence: Presence?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)

data class CheckpointPatchDto(

    @NotNull(message = "A valid userId must be provided")
    var userId: Long,

    @NotNull(message = "A valid SessionId must be provided")
    var sessionId: Long,

    @Pattern(regexp = "ON_TIME|ABSENT|LATE|VERIFIED_LATE|VERIFIED_ABSENT|ONLINE|SICK", message = "Only valid presence types can be provided")
    val presence: String?,

    @Size(max = 2000, message = "An impediment cannot be longer than 2000 characters")
    val impediment: String?,

    @Min(value = 0, message = "Stars must be between 0 and 10")
    @Max(value = 10, message = "Stars must be between 0 and 10")
    val stars: Int?,

    @Size(max = 2000, message = "A comment cannot be longer than 2000 characters")
    val comment: String?,
)

