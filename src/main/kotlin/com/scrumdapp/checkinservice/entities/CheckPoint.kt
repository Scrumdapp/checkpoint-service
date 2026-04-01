package com.scrumdapp.checkinservice.entities

import jakarta.persistence.*
import java.io.Serializable



data class CheckPoint(val userId: Int = 0) : Serializable

@Entity
@IdClass(CheckPoint::class)
@Table(name = "check_in")
class CheckIn {

    @Id
    var userId: Int = 0



    var obstacleComment: String? = null
    var presence: Int? = null
    var presenceComment: String? = null
    var checkinStars: Int? = null
    var checkinComment: String? = null
    var checkupStars: Int? = null
    var checkupComment: String? = null
    var checkoutStars: Int? = null
    var checkoutComment: String? = null
}

