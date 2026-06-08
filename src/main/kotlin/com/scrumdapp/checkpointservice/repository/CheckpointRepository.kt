package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CheckpointRepository: JpaRepository<Checkpoint, Long> {

    @Query("""
        SELECT DISTINCT cp 
        FROM Checkpoint cp
            WHERE cp.checkpointSession.id = :sessionId
                AND cp.checkpointSession.groupId = :groupId
                AND cp.groupUserId = :userId
    """)
    fun findUserCheckpoint(
        @Param("sessionId") sessionId: Long,
        @Param("groupId") groupId: Long,
        @Param("userId") userId: Long,
    ): List<Checkpoint>
    fun findAllByCheckpointSessionId(sessionId: Long): List<Checkpoint>
}