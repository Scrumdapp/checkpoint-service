package com.scrumdapp.checkinservice.services

import com.scrumdapp.checkinservice.dto.GroupCreateDto
import com.scrumdapp.checkinservice.dto.GroupPatchDto
import com.scrumdapp.checkinservice.dto.GroupResponseDto
import com.scrumdapp.checkinservice.entities.Group
import com.scrumdapp.checkinservice.entities.GroupFeature
import com.scrumdapp.checkinservice.mappers.GroupMapper
import com.scrumdapp.checkinservice.repositories.GroupFeatureRepository
import com.scrumdapp.checkinservice.repositories.GroupRepository
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

interface GroupService {
    fun getAllGroups(): List<Group>
    fun getGroupById(id: Int): Group?
    fun addGroupFeatures(id: Int, featureKeys: Set<String>): Group?
    fun createGroup(groupDto: GroupCreateDto): GroupResponseDto
    fun updateGroup(id: Int, groupDto: GroupPatchDto): GroupResponseDto
    fun deleteGroup(id: Int)

}

@Service
class GroupServiceImpl(
    private val groupRepository: GroupRepository,
    private val groupFeatureRepository: GroupFeatureRepository,
) : GroupService {
    override fun getAllGroups(): List<Group> {
        return groupRepository.findAll()
    }

    override fun getGroupById(id: Int): Group? {
        return groupRepository.findGroupById(id)
    }

    override fun addGroupFeatures(id: Int, featureKeys: Set<String>): Group? {
        val group = groupRepository.findGroupById(id)?: return null

        val features = groupFeatureRepository.findAllByKeyIn(featureKeys)

        group.features.addAll(features)
        return groupRepository.save(group)
    }

    override fun createGroup(groupDto: GroupCreateDto): GroupResponseDto {
        val group = GroupMapper.applyCreate(groupDto)
        group.features = findFeaturesByKeys(groupDto.features)
        val savedGroup = groupRepository.save(group)
        return GroupMapper.toDto(savedGroup)
    }

    override fun updateGroup(id: Int, groupDto: GroupPatchDto): GroupResponseDto {
        val group = groupRepository.findGroupById(id) ?: throw ResourceNotFoundException()
        GroupMapper.applyPatch(group, groupDto)

        if (groupDto.features != null) {
            group.features = findFeaturesByKeys(groupDto.features)
        }
        val savedGroup = groupRepository.save(group)

        return GroupMapper.toDto(savedGroup)
    }

    override fun deleteGroup(id: Int) {
        return groupRepository.deleteGroupById(id)
    }

    fun findFeaturesByKeys(keys: Set<String>): MutableSet<GroupFeature> {
        if (keys.isEmpty()) return mutableSetOf()

        val features = groupFeatureRepository.findAllByKeyIn(keys).toMutableSet()
        return features
    }
}
