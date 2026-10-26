package com.scrumdapp.checkpointservice.services

import com.scrumdapp.checkpointservice.dto.CheckpointNamesDto
import com.scrumdapp.checkpointservice.entities.CheckpointNames
import com.scrumdapp.checkpointservice.mappers.CheckpointNamesMapper
import com.scrumdapp.checkpointservice.repository.CheckpointNamesRepository
import org.springframework.stereotype.Service

@Service
class CheckPointNamesService(
    private val checkpointNamesRepository: CheckpointNamesRepository
) {

    fun getCheckPointNames(groupId: Long): CheckpointNamesDto? {
        val entity = checkpointNamesRepository.findByGroupId(groupId) ?: return null

        return CheckpointNamesMapper.toDto(entity)
    }

    fun updateCheckPointNames(
        groupId: Long,
        dto: CheckpointNamesDto
    ): CheckpointNamesDto {
        val entity = checkpointNamesRepository.findByGroupId(groupId)
            ?: CheckpointNames(groupId, dto.checkpointNames)

        entity.checkpoint_names = dto.checkpointNames
        checkpointNamesRepository.save(entity)
        return CheckpointNamesMapper.toDto(entity)
    }
}