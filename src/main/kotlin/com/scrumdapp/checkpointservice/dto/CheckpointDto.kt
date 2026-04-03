package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointResponseDto(
    val id: Int,
    val checkpointSessionId: Int,
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
