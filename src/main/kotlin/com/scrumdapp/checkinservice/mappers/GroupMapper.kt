package com.scrumdapp.checkinservice.mappers

import com.scrumdapp.checkinservice.dto.GroupCreateDto
import com.scrumdapp.checkinservice.dto.GroupFeatureDto
import com.scrumdapp.checkinservice.dto.GroupPatchDto
import com.scrumdapp.checkinservice.dto.GroupResponseDto
import com.scrumdapp.checkinservice.entities.Group

object GroupMapper {
    fun toDto(entity: Group): GroupResponseDto =
        GroupResponseDto(
            id = entity.id,
            name = entity.name,
            background_preference = entity.background_preference,
            icon_preference = entity.icon_preference,
            features = entity.features.map { GroupFeatureDto(it.key, it.description) }.toSet()
        )

    fun applyCreate(dto: GroupCreateDto): Group {
        return Group().apply {
            name = dto.name
            background_preference = dto.background_preference
            icon_preference = dto.icon_preference
        }
    }

    fun applyPatch(entity: Group, dto: GroupPatchDto): Group {
        if (dto.name != null) entity.name = dto.name
        if (dto.background_preference != null) entity.background_preference = dto.background_preference
        if (dto.icon_preference != null) entity.icon_preference = dto.icon_preference
        return entity
    }
}