package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.scrumdapp.checkpointservice.entities.Checkpoint


@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointResponseDto(
    val id: Long,
    val sessionId: Int,
    val groupUserId: Int,
    val presence: Int?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)

data class CheckpointPatchDto(
    val presence: Int?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)
