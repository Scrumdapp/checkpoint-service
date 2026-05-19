package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckpointRepository: JpaRepository<Checkpoint, Long> {

    fun findByCheckpointSessionAndGroupUserId(checkpointSession: CheckpointSession, groupUserId: Long): Checkpoint?
    fun findAllByCheckpointSessionId(sessionId: Long): List<Checkpoint>
    fun findAllByGroupUserId(userId: Long): List<Checkpoint>
    fun existsByGroupUserId(userId: Long): Boolean
    fun existsByCheckpointSessionId(sessionId: Long): Boolean
    fun findByCheckpointSessionIdAndGroupUserId(sessionId: Long, groupUserId: Long): List<Checkpoint>
}