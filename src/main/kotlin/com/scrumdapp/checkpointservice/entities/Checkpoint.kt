package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class CheckPointId(val groupUserId: Long = 0, val checkpointSessionId: Long = 0) : Serializable

@Entity
@Table(name = "check_point")
class Checkpoint(checkpointSession: CheckpointSession) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "checkpoint_session_id")
    var checkpointSession: CheckpointSession = checkpointSession

    var groupUserId: Long = 0


    @Column(columnDefinition = "TEXT")
    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null

    @Column(columnDefinition = "TEXT")
    var comment: String? = null
}