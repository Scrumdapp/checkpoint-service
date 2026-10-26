package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.dto.CheckpointPatchDto
import com.scrumdapp.checkpointservice.dto.CheckpointResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.entities.CheckpointSession
import com.scrumdapp.checkpointservice.groups.GroupRequestService
import com.scrumdapp.checkpointservice.mappers.applyPatch
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import org.springframework.stereotype.Service

@Service
class CheckpointService(
    private val checkpointRepository: CheckpointRepository,
    private val checkpointSessionService: CheckpointSessionService,
    private val groupRequestService: GroupRequestService
) {
    fun upsertCheckpoint(
        groupId: Long,
        dto: CheckpointPatchDto,
        ownId: Long
    ): CheckpointResponseDto {
        val session = checkpointSessionService.getActiveSession(dto.sessionId)

        if (session.groupUserId != ownId && dto.userId != ownId) {
            throw ForbiddenException(message = "Only the owner of a session can alter other users checkpoints")
        }

        var checkpoint = checkpointRepository
            .findUserCheckpoint(session.id, groupId, dto.userId)
            .firstOrNull()

        if (checkpoint != null) {
            checkpoint.applyPatch(dto)
        } else {
           checkpoint = createNewUserCheckpoint(groupId, session, dto)
        }

        return checkpointRepository.save(checkpoint).toDto()
    }

    fun findCheckpointsBySessionId(
        sessionId: Long,
        groupId: Long,
    ): List<CheckpointResponseDto> {
        val checkpoints = checkpointRepository.findAllInSession(sessionId, groupId)
        return checkpoints.map { it.toDto() }
    }

    private fun createNewUserCheckpoint(groupId: Long, session: CheckpointSession, dto: CheckpointPatchDto): Checkpoint {
        val groupUsers = groupRequestService.getGroupUsers(groupId)

        val user = groupUsers.find { it.user_id == dto.userId }
        if (user == null || user.is_ghost) {
            throw BadRequestException(message = "Could not create checkpoint for user ${dto.userId}")
        }

        return Checkpoint(session, dto.userId).applyPatch(dto)
    }

}