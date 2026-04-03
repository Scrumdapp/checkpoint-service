package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class CheckpointSessionService(
    val checkpointSessionRepository: CheckpointSessionRepository
) {

    fun getSessions(groupId: Int): List<CheckpointSession> {
        return checkpointSessionRepository.findAllByGroupId(groupId)
    }

    fun getActiveSessions(groupId: Int): List<CheckpointSession> {
        val sessions = checkpointSessionRepository.findAllByGroupId(groupId)
        return sessions.filter { LocalTime.now().isAfter(it.startTime.plusMinutes(it.durationMinutes.toLong()))  }
    }

    fun getSession(groupId: Int, id: Int): CheckpointSession? {
        return checkpointSessionRepository.findByIdAndGroupId(id, groupId)
    }

    fun createSession(groupId: Int, ownerId: Int, dto: CheckpointSessionCreationDto): CheckpointSession {
        val checkpointSession = dto.toEntity(groupId, ownerId)
        return checkpointSessionRepository.save(checkpointSession)
    }
}