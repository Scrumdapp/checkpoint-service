package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.BadRequestException
import com.scrumdapp.checkpointservice.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.mappers.applyPatch
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Service
class CheckPointService(
    private val checkpointRepository: CheckpointRepository,
    private val checkpointSessionRepository: CheckpointSessionRepository
) {

    fun upsertCheckpoint(sessionId: Int, groupId: Int, groupUserId: Int, dto: CheckpointPatchDto): CheckpointResponseDto {
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

    private fun checkSessionAge(session: CheckpointSession): Boolean {
        if (session.createdDate != LocalDate.now()) return false
        val endTime = session.startTime.plusMinutes(session.durationMinutes.toLong())
        return Duration.between(LocalTime.now(), endTime).toMillis() > 0
    }
}