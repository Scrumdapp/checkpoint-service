package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface CheckpointSessionRepository: JpaRepository<CheckpointSession, Int> {

    fun findFirstById(id: Long): CheckpointSession?
    fun findByIdAndGroupId(id: Long, groupId: Long): CheckpointSession?
    fun findAllByGroupId(groupId: Long): List<CheckpointSession>
    fun findAllByGroupIdAndCreatedDate(groupId: Long, createDate: LocalDate?): List<CheckpointSession>
    fun findAllByGroupIdAndCreatedDateBetween(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<CheckpointSession>
    fun existsById(id: Long): Boolean
}