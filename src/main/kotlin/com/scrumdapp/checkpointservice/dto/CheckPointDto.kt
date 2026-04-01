package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckPointSessionDto(
    val groupuserId: Int,
    val groupId: Int,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val duration: Int,

    )
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckPointDto(
    val groupuserId: Int,
    val comment: String?,
    val presence: Int?,
    val impediment: String?,
    val stars: Int?,
)