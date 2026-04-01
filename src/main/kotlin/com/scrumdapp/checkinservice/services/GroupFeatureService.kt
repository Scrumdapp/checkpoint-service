package com.scrumdapp.checkinservice.services

import com.scrumdapp.checkinservice.entities.GroupFeature
import com.scrumdapp.checkinservice.repositories.GroupFeatureRepository
import org.springframework.stereotype.Service

interface GroupFeatureService {
    fun getFeatures(): List<GroupFeature>
    fun getFeature(key: String): List<GroupFeature>
    fun createFeature(feature: GroupFeature): GroupFeature?
    fun updateFeature(feature: GroupFeature): GroupFeature
    fun deleteFeature(key: String): GroupFeature?
}

@Service
class GroupFeatureServiceImpl(
    private val featureRepository: GroupFeatureRepository
): GroupFeatureService {
    override fun getFeatures(): List<GroupFeature> {
        return featureRepository.findAll()
    }

    override fun getFeature(key: String): List<GroupFeature> {
        return featureRepository.findAllByKey(key)
    }

    override fun createFeature(feature: GroupFeature): GroupFeature? {
        return featureRepository.save(feature)
    }

    override fun updateFeature(feature: GroupFeature): GroupFeature {
        TODO("Not yet implemented")
    }

    override fun deleteFeature(key: String): GroupFeature? {
        TODO("Not yet implemented")
    }
}