package com.scrumdapp.checkpointservice.repositories

import com.scrumdapp.checkpointservice.entities.Checkpoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckpointRepository: JpaRepository<Checkpoint, Long> {

    fun findAllByCommentIsFalse(): List<Checkpoint>
}