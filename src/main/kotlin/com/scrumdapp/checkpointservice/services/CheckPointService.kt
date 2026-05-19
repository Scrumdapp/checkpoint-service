package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.configs.BadRequestException
import com.scrumdapp.checkpointservice.configs.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.mappers.applyPatch
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Service
class CheckPointService(
    private val checkpointRepository: CheckpointRepository,
    private val checkpointSessionRepository: CheckpointSessionRepository
) {

    fun upsertCheckpoint(sessionId: Long, groupId: Long, groupUserId: Long, dto: CheckpointPatchDto): CheckpointResponseDto {
        val session = checkpointSessionRepository.findByIdAndGroupId(sessionId, groupId)
            ?:
            throw NotFoundException(message =  "Checkpoint with id $sessionId not found")

        if (!checkSessionAge(session)) {
            throw BadRequestException(message = "Checkpoint with id $sessionId has expired")
        }

        // To Do check if user is actually present in group

        val existing = checkpointRepository.findByCheckpointSessionAndGroupUserId(session, groupUserId)

        val checkpoint = existing?.applyPatch(dto) ?: dto.toEntity(session, groupUserId)

        return checkpointRepository.save(checkpoint).toDto()
    }

    fun findAllBySessionId(sessionId: Long): List<CheckpointResponseDto> {
        if (!checkpointRepository.existsByCheckpointSessionId(sessionId)) throw NotFoundException(message = "No checkpoints found for session $sessionId")
        return checkpointRepository.findAllByCheckpointSessionId(sessionId).map { it.toDto() }
    }
    
    fun findAllByGroupUserId(userId: Long): List<CheckpointResponseDto> {
        if (!checkpointRepository.existsByGroupUserId(userId)) throw NotFoundException(message = "No checkpoints found for user $userId")
        return checkpointRepository.findAllByGroupUserId(userId).map { it.toDto() }
    }

    fun findAllBySessionIdAndGroupUserId(sessionId: Long, groupUserId: Long): List<CheckpointResponseDto> {
        val checkpoints = checkpointRepository.findByCheckpointSessionIdAndGroupUserId(sessionId, groupUserId)
        if (checkpoints.isEmpty()) { throw NotFoundException(message = "No checkpoints found for user $groupUserId in session $sessionId") }
        return checkpoints.map { it.toDto() }
    }



    private fun checkSessionAge(session: CheckpointSession): Boolean {
        if (session.createdDate != LocalDate.now()) return false
        val endTime = session.startTime.plusMinutes(session.durationMinutes.toLong())
        return Duration.between(LocalTime.now(), endTime).toMillis() > 0
    }
}