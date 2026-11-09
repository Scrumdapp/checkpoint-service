package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.entities.CheckpointNames
import com.scrumdapp.checkpointservice.mappers.patchFromDto
import com.scrumdapp.checkpointservice.mappers.toDto
import com.scrumdapp.checkpointservice.repository.CheckpointNamesRepository
import org.springframework.stereotype.Service

@Service
class CheckpointNameService(
    private val checkpointNamesRepository: CheckpointNamesRepository
) {

    fun getCheckpointNames(groupId: Long): CheckpointNamesDto {
        return checkpointNamesRepository.findByGroupId(groupId)?.toDto() ?: CheckpointNamesDto(emptyList())
    }

    fun updateCheckPointNames(
        groupId: Long,
        dto: CheckpointNamesDto
    ): CheckpointNamesDto {
        val entity = checkpointNamesRepository.findByGroupId(groupId)
            ?: CheckpointNames(groupId, dto.checkpointNames)
        val patchedEntity = checkpointNamesRepository.save(entity.patchFromDto(dto))
        return patchedEntity.toDto()
    }
}