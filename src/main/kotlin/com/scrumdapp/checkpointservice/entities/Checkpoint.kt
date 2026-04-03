package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class CheckPointId(val groupUserId: Int = 0, val checkpointSessionId: Int = 0) : Serializable

@Entity
@Table(name = "check_point")
class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    val id: Long = 0

    @Column(name = "checkpoint_session_id", insertable = false, updatable = false)
    var checkpointSessionId: Int = 0

    @ManyToOne
    @JoinColumn(name = "checkpoint_session_id")
    var checkpointSession: CheckpointSession? = null

    var groupUserId: Int = 0

    @Column(columnDefinition = "TEXT")
    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null

    @Column(columnDefinition = "TEXT")
    var comment: String? = null
}