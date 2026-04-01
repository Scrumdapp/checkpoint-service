package com.scrumdapp.checkinservice.mappers

import com.scrumdapp.checkinservice.dto.CheckInDto
import com.scrumdapp.checkinservice.entities.CheckIn

fun CheckIn.toDto(): CheckInDto {
    return CheckInDto(
        userId = userId,
        groupId = groupId,
        date = date,


        obstacle_comment = obstacleComment,
        presence = presence,
        presence_comment = presenceComment,

        checkin_stars = checkinStars,
        checkin_comment = checkinComment,

        checkup_stars = checkupStars,
        checkup_comment = checkupComment,

        checkout_stars = checkoutStars,
        checkout_comment = checkoutComment
    )
}

fun CheckInDto.toEntity(): CheckIn {
    return CheckIn().apply {
        userId = this@toEntity.userId
        groupId = this@toEntity.groupId
        date = this@toEntity.date

        obstacleComment = this@toEntity.obstacle_comment
        presence = this@toEntity.presence
        presenceComment = this@toEntity.presence_comment

        checkinStars = this@toEntity.checkin_stars
        checkinComment = this@toEntity.checkin_comment

        checkupStars = this@toEntity.checkup_stars
        checkupComment = this@toEntity.checkup_comment

        checkoutStars = this@toEntity.checkout_stars
        checkoutComment = this@toEntity.checkout_comment
    }
}