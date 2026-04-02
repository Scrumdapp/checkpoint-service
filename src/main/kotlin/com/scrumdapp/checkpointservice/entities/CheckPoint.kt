package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.io.Serializable



data class CheckPoint(val groupUserId: Int = 0, val checkpointSessionId: Int = 0) : Serializable

@Entity

@Table(name = "check_point")
class Checkpoint {


    @Id
    var checkpointId: Long? = null
    @Id
    var groupUserId: Int = 0

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    @Id
    var id: CheckpointSession? = null

    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null
    var comment: String? = null
}