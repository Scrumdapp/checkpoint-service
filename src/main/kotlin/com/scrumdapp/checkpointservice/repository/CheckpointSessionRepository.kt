package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.dto.SessionDateDtoRaw
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface CheckpointSessionRepository: JpaRepository<CheckpointSession, Int> {

    @Query(
        value = """
        SELECT 
            cs.created_date AS "createdDate",
            cs.id AS "id"
        FROM (
          SELECT id, created_date, DENSE_RANK() OVER (ORDER BY created_date DESC) AS rk
          FROM checkpoint_sessions
          WHERE created_date <= :startDate 
          AND group_id = :groupId
        ) cs
        WHERE cs.rk <= :limit
        ORDER BY cs.created_date DESC
      """,
        nativeQuery = true
    )
    fun findRecentSessionDates(
        @Param("startDate") startDate: LocalDate,
        @Param("groupId") groupId: Long,
        @Param("limit") limit: Int
    ): List<SessionDateDtoRaw>

    fun findByIdAndGroupId(id: Long, groupId: Long): CheckpointSession?
    fun findAllByGroupId(groupId: Long): List<CheckpointSession>
    fun findAllByGroupIdAndCreatedDate(groupId: Long, createDate: LocalDate?): List<CheckpointSession>
    fun findAllByGroupIdAndCreatedDateBetween(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<CheckpointSession>
}