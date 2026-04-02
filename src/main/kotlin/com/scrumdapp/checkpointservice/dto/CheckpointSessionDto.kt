package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate
import java.time.LocalTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointSessionDto(
    val groupuserId: Int,
    val groupId: Int,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val duration: Int,

    )