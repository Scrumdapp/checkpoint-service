package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.groups.GroupRequestService
import com.scrumdapp.checkpointservice.mappers.applyPatch
import com.scrumdapp.checkpointservice.mappers.isActive
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.stereotype.Service

@Service
class CheckPointService(
    private val checkpointRepository: CheckpointRepository,
    private val sessionRepository: CheckpointSessionRepository,
    private val groupRequestService: GroupRequestService
) {
    fun upsert(
        groupId: Long,
        dto: CheckpointPatchDto,
        ownId: Long
    ): CheckpointResponseDto {
        val session = getActiveSession(dto.sessionId)

        if (session.groupUserId != ownId && dto.userId != ownId) {
            throw ForbiddenException(message = "Only the owner of a session can alter other users checkpoints")
        }

        var checkpoint = checkpointRepository
            .findUserCheckpoint(session.id, groupId, dto.userId)
            .firstOrNull()

        if (checkpoint != null) {
            checkpoint.applyPatch(dto)
        } else {
           checkpoint = create(groupId, session, dto)
        }

        return checkpointRepository.save(checkpoint).toDto()
    }

    fun findAllBySession(
        sessionId: Long,
    ): List<CheckpointResponseDto> {
        val checkpoints = checkpointRepository.findAllByCheckpointSessionId(sessionId)

        return if (checkpoints.isEmpty()) {
            emptyList()
        } else {
            checkpoints.map { it.toDto() }
        }
    }

    private fun create(groupId: Long, session: CheckpointSession, dto: CheckpointPatchDto): Checkpoint {
        checkGroupAccess(groupId, dto.userId)

        return Checkpoint(session, dto.userId).applyPatch(dto)
    }

    private fun getActiveSession(sessionId: Long): CheckpointSession {
        val session = sessionRepository.findById(sessionId) ?:
            throw BadRequestException(message = "Session with id $sessionId not found")

        if (!session.isActive()) {
            throw BadRequestException(message = "Session has expired")
        }
        return session
    }

    private fun checkGroupAccess(groupId: Long, userId: Long) {
        if (userId !in groupRequestService.getGroupUserIds(groupId)) {
            throw ForbiddenException(message = "Could not create checkpoint for user $userId")
        }
    }
}