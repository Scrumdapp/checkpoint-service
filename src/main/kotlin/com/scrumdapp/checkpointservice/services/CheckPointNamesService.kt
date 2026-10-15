package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.entities.CheckpointNames
import com.scrumdapp.checkpointservice.errors.NotFoundException
import com.scrumdapp.checkpointservice.mappers.CheckpointNamesMapper
import com.scrumdapp.checkpointservice.repository.CheckpointNamesRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class CheckPointNamesService(
    private val checkpointNamesRepository: CheckpointNamesRepository
) {

    fun getCheckPointNames(groupId: Int): CheckpointNamesDto {
        val entity = checkpointNamesRepository.findByGroupId(groupId)
            ?: throw NotFoundException(message = "Checkpoint names not found")

        return CheckpointNamesMapper.toDto(entity)
    }

    fun updateCheckPointNames(
        groupId: Int,
        dto: CheckpointNamesDto
    ): CheckpointNamesDto {
        val entity = checkpointNamesRepository.findByGroupId(groupId)
            ?.apply {
                checkpoint_names = dto.checkpointNames
            }
            ?: CheckpointNames(
                group_id = groupId,
                checkpointNames = dto.checkpointNames
            )

        return CheckpointNamesMapper.toDto(
            checkpointNamesRepository.save(entity)
        )
    }
}