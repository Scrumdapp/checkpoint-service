package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.groups.GroupRequestService
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class CheckpointSessionService(
    private val checkpointSessionRepository: CheckpointSessionRepository,
    private val checkpointRepository: CheckpointRepository,
    private val groupRequestService: GroupRequestService,
) {

    fun getSessions(groupId: Long, date: LocalDate?): List<CheckpointSessionResponseDto> {

        val sessions = if (date != null) {
            checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, date)
        } else {
            checkpointSessionRepository.findAllByGroupId(groupId)
        }
        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in sessions) {
            response.add(session.toDto())
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
            response.add(session.toDto())
        }

        return response.toList()
    }

    fun getSession(groupId: Long, id: Long): CheckpointSessionResponseDto? {
        val session = checkpointSessionRepository.findByIdAndGroupId(id, groupId) ?: throw BadRequestException(message = "Checkpoint with id $id not found")

        return session.toDto()
    }
    fun getSessionsBetweenDates(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<CheckpointSessionResponseDto> {
        val sessions = checkpointSessionRepository.findAllByGroupIdAndCreatedDateBetween(groupId, startDate, endDate)
        val response = mutableListOf<CheckpointSessionResponseDto>()
        for (session in sessions) {
            response.add(session.toDto())
        }
        return response.toList()
    }

    fun createSession(jwt: Jwt, groupId: Long, ownerId: Long, dto: CheckpointSessionCreationDto): CheckpointSessionResponseDto {
        val checkpointSession = dto.toEntity(groupId, ownerId, dto.name)

        val groupUsers = groupRequestService.getGroupUserIds(jwt, groupId)
        val session = checkpointSessionRepository.save(checkpointSession)

        for (groupUser in groupUsers) {
            checkpointRepository.save(Checkpoint(session, groupUser))
        }
        return session.toDto()
    }
}