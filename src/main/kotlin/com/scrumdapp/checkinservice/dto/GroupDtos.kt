package com.scrumdapp.checkinservice.dto

import com.scrumdapp.checkinservice.utils.validators.FeatureValidRegex
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank


data class GroupResponseDto(
    val id: Int,
    val name: String?,
    val background_preference: Int?,
    val icon_preference: Int?,
    val features: Set<GroupFeatureDto>
)

data class GroupCreateDto(

    @field:NotBlank(message = "Name is required")
    val name: String,
    val background_preference: Int? = null,
    val icon_preference: Int? = null,

    @field:FeatureValidRegex
    val features: Set<String> = emptySet()
)

data class GroupPatchDto(
    val name: String? = null,
    val background_preference: Int? = null,
    val icon_preference: Int? = null,
    @field:FeatureValidRegex
    val features: Set<String>? = null
)

data class GroupFeatureDto(

    val key: String,
    val description: String?
)



