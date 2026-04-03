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
    var id: Int = 0

    var groupId: Int = 0
    var groupUserId: Int = 0

    val createdDate: LocalDate = LocalDate.now()

    var startTime: LocalTime = LocalTime.now()
    var durationMinutes: Int? = null
}