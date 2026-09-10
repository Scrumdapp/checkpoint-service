package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.SessionDateResponseDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.groups.GroupRequestService
import com.scrumdapp.checkpointservice.mappers.isActive
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.mappers.toEntity
import com.scrumdapp.checkpointservice.mappers.toSessionDateResponse
import com.scrumdapp.checkpointservice.repository.CheckpointRepository
import com.scrumdapp.checkpointservice.repository.CheckpointSessionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class CheckpointSessionService(
    private val checkpointSessionRepository: CheckpointSessionRepository,
    private val checkpointRepository: CheckpointRepository,
    private val groupRequestService: GroupRequestService,
) {

    fun getSessions(groupId: Long, date: LocalDate?): List<SessionResponseDto> {
        val sessions = date
            ?.let { checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, it) }
            ?: checkpointSessionRepository.findAllByGroupId(groupId)
        return sessions.map { it.toDto() }
    }

    fun getActiveSessions(groupId: Long, date: LocalDate?): List<SessionResponseDto> {
        val sessions = date
            ?.let { checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, it) }
            ?: checkpointSessionRepository.findAllByGroupId(groupId)
        sessions.filterNot { it.isActive() }

        return sessions.map { it.toDto() }
    }

    fun getRecentSessions(groupId: Long, limit: Int): SessionDateResponseDto {
        val sessions = checkpointSessionRepository.findRecentSessionDates(LocalDate.now(), groupId, limit)

        return sessions.toSessionDateResponse()
    }

    fun getRecentCalendarSessions(groupId: Long, month: YearMonth, limit: Int): SessionDateResponseDto {
        val startDate = month.atDay(1)
        val endDate = month.atEndOfMonth()

        val sessions = checkpointSessionRepository.findSessionDatesBetweenDates(startDate, endDate, groupId, limit)
        return sessions.toSessionDateResponse()
    }

    fun getSession(groupId: Long, id: Long): SessionResponseDto {
        val session = checkpointSessionRepository.findByIdAndGroupId(id, groupId) ?:
            throw NotFoundException(message = "Session with id $id not found")
        return session.toDto()
    }

    fun getSessionsBetweenDates(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<SessionResponseDto> {
        val sessions = checkpointSessionRepository.findAllByGroupIdAndCreatedDateBetween(groupId, startDate, endDate)
        return sessions.map { it.toDto() }
    }

    fun getMonthsWithSessions(groupId: Long): List<String> {
        val dates = checkpointSessionRepository.findMonthsWithSessions(groupId)

        return dates
            .map(YearMonth::from)
            .distinct()
            .sorted()
            .map(YearMonth::toString)
    }

    fun createSession(groupId: Long, ownerId: Long, dto: CheckpointSessionCreationDto): SessionResponseDto {
        val session = checkpointSessionRepository.save(dto.toEntity(groupId, ownerId, dto.name))

        val userIds = groupRequestService.getGroupUserIds(groupId)

        val checkpoints = userIds.map { userId -> Checkpoint(session, userId) }

        checkpointSessionRepository.saveAll(checkpoints)
        return session.toDto()
    }
}