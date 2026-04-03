package com.scrumdapp.checkpointservice.repository

import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository

interface CheckpointSessionRepository: JpaRepository<CheckpointSession, Int> {

    fun findByIdAndGroupId(id: Int, groupId: Int): CheckpointSession?
    fun findAllByGroupId(groupId: Int): List<CheckpointSession>
}