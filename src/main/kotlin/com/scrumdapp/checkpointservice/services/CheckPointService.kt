package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import org.springframework.stereotype.Service

@Service
class CheckpointSession {

    fun getCheckpointSession(sessionId: Int): List<Checkpoint> {

    }

    fun createCheckpoint(sessionId: Int, groupId: Int, dto: CheckpointPatchDto) {

    }
}