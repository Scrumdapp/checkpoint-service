package com.scrumdapp.checkpointservice.repositories

import com.scrumdapp.checkpointservice.entities.CheckpointSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckpointSessionsRepository: JpaRepository<CheckpointSession, Long> {

    fun findAllByGroupId(groupId: Int): List<CheckpointSession>
}