package com.scrumdapp.checkinservice.entities

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.Duration

@Embeddable
data class CheckPointSessionId(val userId: Int = 0, val checkpointId: Int = 0) : Serializable

@Entity
@Table(name = "checkpoint_sessions")
class CheckPointSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    var groupId: Int = 0

    var userId: Int = 0

    var startDate: LocalDate? = null

    var startTime: LocalTime? = null

    @Column(name = "duration_minutes")
    var durationMinutes: Long? = null

    var duration: Duration?
        get() = durationMinutes?.let { Duration.ofMinutes(it) }
        set(value) { durationMinutes = value?.toMinutes() }
}