package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
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

    @CreatedDate
     val createdDate: Instant? = null
    var startTime: LocalTime? = null
    var durationMinutes: Int? = null
}