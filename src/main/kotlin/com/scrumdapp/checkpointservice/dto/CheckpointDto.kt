package com.scrumdapp.checkpointservice.dto

import com.fasterxml.jackson.annotation.JsonInclude

enum class Presence(val code: Int) {
    ON_TIME(1),
    ABSENT(2),
    LATE(3),
    VERIFIED_LATE(4),
    VERIFIED_ABSENT(5),
    ONLINE(6),
    SICK(7);

    companion object {
        fun fromCode(code: Int): Presence? = entries.find { it.code == code }
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckpointResponseDto(
    val id: Long,
    val sessionId: Long,
    val groupUserId: Long,
    val presence: Presence?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)

data class CheckpointPatchDto(
    val groupUserId: Int,
    val presence: Presence?,
    val impediment: String?,
    val stars: Int?,
    val comment: String?,
)

