package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime




@Entity
@Table(name = "checkpoint_sessions")
class CheckPointSession {


    var groupId: Int = 0
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    var id: Int = 0

    var groupuserId: Int = 0

    var startDate: LocalDate? = null

    var startTime: LocalTime? = null

    var endTime: LocalTime? = null

    @Column(name = "duration_minutes")
    var durationMinutes: Int? = null
}