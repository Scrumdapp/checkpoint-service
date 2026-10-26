package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.CheckpointNames
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckpointNamesRepository: JpaRepository<CheckpointNames, Int> {
    fun findByGroupId(group_id: Int): CheckpointNames?
}