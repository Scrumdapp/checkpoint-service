package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.configs.ForbiddenException
import com.scrumdapp.checkpointservice.configs.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPartialDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.mappers.toPartialDto
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class CheckpointSessionService(
    val checkpointSessionRepository: CheckpointSessionRepository,
    val checkpointRepository: CheckpointRepository
) {

    fun getSessions(groupId: Long, date: LocalDate?): List<CheckpointSessionResponseDto> {

        val sessions = if (date != null) {
            checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, date)
        } else {
            checkpointSessionRepository.findAllByGroupId(groupId)
        }
        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in sessions) {
            val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)
            response.add(session.toDto(checkpoints))
        }

        return response.toList()
    }

    fun getActiveSessions(groupId: Long, date: LocalDate?): List<CheckpointSessionResponseDto> {
        val sessions = if (date != null) {
            checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, date)
        } else {
            checkpointSessionRepository.findAllByGroupId(groupId)
        }

        val filteredSessions = sessions.filter {
            val now = LocalTime.now()
            val endTime = it.startTime.plusMinutes(it.durationMinutes.toLong())
            now.isAfter(it.startTime) && now.isBefore(endTime)
        }

        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in filteredSessions) {
            val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)
            response.add(session.toDto(checkpoints))
        }

        return response.toList()
    }

    fun getSession(groupId: Long, id: Long): CheckpointSessionResponseDto? {
        val session = checkpointSessionRepository.findByIdAndGroupId(id, groupId) ?: throw NotFoundException(message = "Checkpoint with id $id not found")

        val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)

        return session.toDto(checkpoints)
    }
    fun getSessionsBetweenDates(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<CheckpointSessionResponseDto> {
        val sessions = checkpointSessionRepository.findAllByGroupIdAndCreatedDateBetween(groupId, startDate, endDate)
        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in sessions) {
            val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)
            response.add(session.toDto(checkpoints))
        }
        return response.toList()
    }

    fun getSessionOnDate(groupId: Long, date: LocalDate): List<CheckpointSessionResponseDto> {
        val sessions = checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, date)
        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in sessions) {
            val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)
            response.add(session.toDto(checkpoints))
        }
        return response.toList()
    }
    fun getPartialSession(groupId: Long, id: Long): CheckpointSessionPartialDto? {
        val session = checkpointSessionRepository.findByIdAndGroupId(id, groupId) ?: throw NotFoundException(message = "Checkpoint with id $id not found")
        return session.toPartialDto()
    }

    fun createSession(groupId: Long, ownerId: Long, dto: CheckpointSessionCreationDto): CheckpointSessionResponseDto {
        val checkpointSession = dto.toEntity(groupId, ownerId)
        return checkpointSessionRepository.save(checkpointSession).toDto(emptyList())
    }

    fun disableSession(id: Long, userId: Long): CheckpointSessionResponseDto {
        val session = checkpointSessionRepository.findFirstById(id) ?: throw NotFoundException(message = "Checkpoint with id $id not found")

        if (session.groupUserId != userId) throw ForbiddenException(message = "Only the owner of the session can modify it")

        // Disables the session by forcing the remaining time to 0
        session.durationMinutes = 0
        val newSession = checkpointSessionRepository.save(session)

        val checkpoints = checkpointRepository.findAllByCheckpointSessionId(session.id)
        return newSession.toDto(checkpoints)
    }
}