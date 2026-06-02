package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime


@Entity
@Table(name = "checkpoint_sessions")
class CheckpointSession {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    var id: Long = 0

    var groupId: Long = 0
    var groupUserId: Long = 0

    val createdDate: LocalDate = LocalDate.now()

    var startTime: LocalTime = LocalTime.now()
    var durationMinutes: Int = 15

    var name: String? = null


    @OneToMany(mappedBy = "checkpointSession", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var checkpoints: MutableList<Checkpoint> = mutableListOf()
}