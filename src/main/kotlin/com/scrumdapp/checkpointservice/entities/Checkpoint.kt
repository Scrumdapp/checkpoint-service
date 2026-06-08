package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*

@Entity
@Table(name = "check_point")
class Checkpoint(session: CheckpointSession, groupUser: Long) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    val id: Long = 0

    @ManyToOne
    @JoinColumn(name = "checkpoint_session_id")
    var checkpointSession: CheckpointSession = session

    var groupUserId: Long = groupUser


    @Column(columnDefinition = "TEXT")
    var impediment: String? = null
    var presence: Int? = null
    var stars: Int? = null

    @Column(columnDefinition = "TEXT")
    var comment: String? = null
}