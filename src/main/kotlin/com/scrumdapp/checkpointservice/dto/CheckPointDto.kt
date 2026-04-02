package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointDto(
    val groupuserId: Int,
    val comment: String?,
    val presence: Int?,
    val impediment: String?,
    val stars: Int?,
)