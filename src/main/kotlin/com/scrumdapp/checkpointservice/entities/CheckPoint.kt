package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.io.Serializable



data class CheckPoint(val groupuserId: Int = 0, val Id: Int = 0) : Serializable

@Entity
@IdClass(CheckPoint::class)
@Table(name = "check_point")
class Checkpoint {

    @Id
    var groupuserId: Int = 0

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    @Id
    var id: Int = 0

    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null
    var comment: String? = null
}