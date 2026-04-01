package com.scrumdapp.checkinservice.services

import com.scrumdapp.checkinservice.dto.CheckInDto
import com.scrumdapp.checkinservice.entities.CheckInId
import com.scrumdapp.checkinservice.mappers.toDto
import com.scrumdapp.checkinservice.mappers.toEntity
import com.scrumdapp.checkinservice.repositories.CheckInRepository
import org.springframework.stereotype.Service
import java.time.LocalDate


interface CheckInService {

    fun findById(id: CheckInId): CheckInDto?

    fun deleteById(id: CheckInId)

    fun saveCheckIn(checkIn: CheckInDto): CheckInDto



    fun findByGroupId(groupId: Int): List<CheckInDto>

    fun findByUserAndDateRange(
        groupId: Int,
        userId: Int,
        start: LocalDate,
        end: LocalDate
    ): List<CheckInDto>

    fun findByGroupIdAndDate(
        groupId: Int,
        date: LocalDate,
    ): List<CheckInDto>

    fun updateUserCheckIn(checkIn: CheckInDto): CheckInDto

}

@Service
class CheckInServiceImpl(
    private val checkInRepository: CheckInRepository,
) : CheckInService {

    override fun findById(id: CheckInId): CheckInDto? {
        return checkInRepository.findById(id)
            .orElse(null)
            ?.toDto()
    }



    override fun deleteById(id: CheckInId) {
        checkInRepository.deleteById(id)
    }

    override fun saveCheckIn(checkIn: CheckInDto): CheckInDto {
        val saved = checkInRepository.save(checkIn.toEntity())
        return saved.toDto()

    }

    override fun updateUserCheckIn(checkIn: CheckInDto): CheckInDto {
        val updated = checkInRepository.save(checkIn.toEntity())
        return updated.toDto()
    }

    override fun findByGroupId(groupId: Int): List<CheckInDto> {
        return checkInRepository.findByGroupId(groupId)
            .map { it.toDto() }
    }

    override fun findByUserAndDateRange(
        groupId: Int,
        userId: Int,
        start: LocalDate,
        end: LocalDate
    ): List<CheckInDto> {
        return checkInRepository
            .findByGroupIdAndUserIdAndDateBetween(groupId, userId, start, end)
            .map { it.toDto() }
    }

    override fun findByGroupIdAndDate(
        groupId: Int, date: LocalDate
    ): List<CheckInDto> {
        return checkInRepository.findByGroupIdAndDate(groupId, date).map { it.toDto() }
    }
}