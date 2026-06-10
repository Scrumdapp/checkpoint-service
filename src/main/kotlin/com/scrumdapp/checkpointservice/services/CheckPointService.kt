package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.mappers.applyPatch
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Service
class CheckPointService(
    private val checkpointRepository: CheckpointRepository,
) {

    fun upsertCheckpoint(groupId: Long, dto: CheckpointPatchDto, ownId: Long): CheckpointResponseDto {

        val sessionId = dto.sessionId
        val checkpoints = checkpointRepository.findUserCheckpoint(sessionId, groupId, dto.userId)

        if (checkpoints.isEmpty()) {
            throw BadRequestException(message = "Could not find checkpoint")
        }

        val checkpoint = checkpoints.first()

        if (!checkSessionAge(checkpoint.checkpointSession)) {
            throw BadRequestException(message = "Checkpoint with id $sessionId has expired")
        }

        if (checkpoint.checkpointSession.groupUserId != ownId && dto.userId != ownId) {
            throw ForbiddenException(message = "Only the owner of a session can alter other users checkpoints")
        }

        return checkpointRepository.save(checkpoint.applyPatch(dto)).toDto()
    }

    fun findAllBySessionId(sessionId: Long, groupId: Long): List<CheckpointResponseDto> {
        val checkpoints = checkpointRepository.findAllByCheckpointSessionId(sessionId)

        if (checkpoints.isEmpty()) return checkpoints.map { it.toDto() }
        if (checkpoints[0].checkpointSession.groupId != groupId) throw ForbiddenException()
        return checkpoints.map { it.toDto() }
    }

    private fun checkSessionAge(session: CheckpointSession): Boolean {
        if (session.createdDate != LocalDate.now()) return false
        val endTime = session.startTime.plusMinutes(session.durationMinutes.toLong())
        return Duration.between(LocalTime.now(), endTime).toMillis() > 0
    }
}