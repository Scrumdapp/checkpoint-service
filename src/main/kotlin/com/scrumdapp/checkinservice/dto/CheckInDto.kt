package com.scrumdapp.checkinservice.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckInDto(
    val userId: Int,
    val groupId: Int,
    val date: LocalDate?,

    val obstacle_comment: String?,
    val presence: Int?,
    val presence_comment: String?,

    val checkin_stars: Int?,
    val checkin_comment: String?,

    val checkup_stars: Int?,
    val checkup_comment: String?,

    val checkout_stars: Int?,
    val checkout_comment: String?
)