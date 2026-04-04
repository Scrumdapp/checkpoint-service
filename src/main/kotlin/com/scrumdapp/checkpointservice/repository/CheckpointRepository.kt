package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckpointRepository: JpaRepository<Checkpoint, Long> {

    fun findByCheckpointSessionAndGroupUserId(checkpointSession: CheckpointSession, groupUserId: Int): Checkpoint?
    fun findAllByCheckpointSessionId(sessionId: Int): List<Checkpoint>
    fun findAllByGroupUserIdAndCheckpointSessionId(userId: Int, checkpointSessionId: Int): List<Checkpoint>
}