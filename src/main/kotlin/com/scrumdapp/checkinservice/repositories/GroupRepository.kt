package com.scrumdapp.checkinservice.repositories

import com.scrumdapp.checkinservice.entities.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository: JpaRepository<Group, Int>{

    fun findGroupById(id: Int): Group?
    fun deleteGroupById(id: Int)
}

