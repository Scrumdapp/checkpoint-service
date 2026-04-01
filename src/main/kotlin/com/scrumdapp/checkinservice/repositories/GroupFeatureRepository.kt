package com.scrumdapp.checkinservice.repositories

import com.scrumdapp.checkinservice.entities.GroupFeature
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupFeatureRepository: JpaRepository<GroupFeature, String> {
    fun findAllByKey(key: String): List<GroupFeature>
    fun findAllByKeyIn(keys: Set<String>): Set<GroupFeature>
}