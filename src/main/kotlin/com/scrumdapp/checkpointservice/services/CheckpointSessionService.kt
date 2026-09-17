package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointSessionCreationDto
import com.scrumdapp.checkpointservice.dto.CheckpointSessionPatchDto
import com.scrumdapp.checkpointservice.dto.SessionDateResponseDto
import com.scrumdapp.checkpointservice.dto.SessionResponseDto
import com.scrumdapp.checkpointservice.entities.Checkpoint
import com.scrumdapp.checkpointservice.errors.BadRequestException
import com.scrumdapp.checkpointservice.errors.ForbiddenException
import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.groups.GroupRequestService
import com.scrumdapp.checkpointservice.mappers.applyPatch
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

    fun getAll(groupId: Long, date: LocalDate?): List<SessionResponseDto> {
        val sessions = date
            ?.let { checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, it) }
            ?: checkpointSessionRepository.findAllByGroupId(groupId)
        return sessions.map { it.toDto() }
    }

    fun getActive(groupId: Long, date: LocalDate?): List<SessionResponseDto> {
        val sessions = date
            ?.let { checkpointSessionRepository.findAllByGroupIdAndCreatedDate(groupId, it) }
            ?: checkpointSessionRepository.findAllByGroupId(groupId)
        return sessions
            .filter { it.isActive() }
            .map { it.toDto() }
    }

    fun getById(groupId: Long, id: Long): SessionResponseDto {
        val session = checkpointSessionRepository.findByIdAndGroupId(id, groupId) ?:
        throw NotFoundException(message = "Session with id $id not found")
        return session.toDto()
    }

    fun getBetweenDates(groupId: Long, startDate: LocalDate, endDate: LocalDate): List<SessionResponseDto> {
        val sessions = checkpointSessionRepository.findAllByGroupIdAndCreatedDateBetween(groupId, startDate, endDate)
        return sessions.map { it.toDto() }
    }

    fun getRecent(groupId: Long, limit: Int): SessionDateResponseDto {
        val sessions = checkpointSessionRepository.findRecentSessionDates(LocalDate.now(), groupId, limit)

        return sessions.toSessionDateResponse()
    }

    fun getInMonths(groupId: Long, month: YearMonth, limit: Int): SessionDateResponseDto {
        val startDate = month.atDay(1)
        val endDate = month.atEndOfMonth()

        val sessions = checkpointSessionRepository.findSessionDatesBetweenDates(startDate, endDate, groupId, limit)
        return sessions.toSessionDateResponse()
    }



    fun getMonthsWithSessions(groupId: Long): List<String> {
        val dates = checkpointSessionRepository.findMonthsWithSessions(groupId)

        return dates
            .map(YearMonth::from)
            .distinct()
            .sorted()
            .map(YearMonth::toString)
    }

    fun create(groupId: Long, ownerId: Long, dto: CheckpointSessionCreationDto): SessionResponseDto {
        val session = checkpointSessionRepository.save(dto.toEntity(groupId, ownerId, dto.name))

        val userIds = groupRequestService.getGroupUserIds(groupId)

        val checkpoints = userIds.map { userId -> Checkpoint(session, userId) }
        checkpointRepository.saveAll(checkpoints)

        return session.toDto()
    }

    fun patch(groupId: Long, sessionId: Long, userId: Long, dto: CheckpointSessionPatchDto): SessionResponseDto {
        val session = checkpointSessionRepository.findByIdAndGroupId(sessionId, groupId) ?:
            throw BadRequestException(message = "Session with id $sessionId not found")

        if (!session.isActive()) throw BadRequestException(message = "Session has expired")

        if (session.groupUserId != userId) throw ForbiddenException(message = "Only the owner of a session can alter the session")

        return checkpointSessionRepository.save(session.applyPatch(dto)).toDto()
    }
}