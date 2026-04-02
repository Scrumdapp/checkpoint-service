package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*


@Entity
@Table(name = "check_point")
class Checkpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    val id: Long? = null

    var checkpointSessionId: Int? = null

    @ManyToOne
    @MapsId("checkpointSessionId")
    @JoinColumn(name = "checkpoint_session_id")
    var checkpointSession: CheckpointSession? = null

    var groupUserId: Int? = null

    @Column(columnDefinition = "TEXT")
    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null

    @Column(columnDefinition = "TEXT")
    var comment: String? = null
}