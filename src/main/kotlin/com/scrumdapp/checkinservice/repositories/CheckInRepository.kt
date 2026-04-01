package com.scrumdapp.checkinservice.repositories

import java.util.Date
import com.scrumdapp.checkinservice.entities.CheckIn
import com.scrumdapp.checkinservice.entities.CheckInId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CheckInRepository : JpaRepository<CheckIn, CheckInId> {

    fun findByGroupId(groupId: Int): List<CheckIn>

    fun findByGroupIdAndUserIdAndDateBetween(
        groupId: Int,
        userId: Int,
        start: LocalDate,
        end: LocalDate
    ): List<CheckIn>


    fun findByGroupIdAndDate(
        groupId: Int,
        date: LocalDate
    ): List<CheckIn>
}