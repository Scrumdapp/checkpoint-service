package com.scrumdapp.checkpointservice.dto

enum class Presence(val code: Int) {
    ON_TIME(1),
    ABSENT(2),
    LATE(3),
    VERIFIED_LATE(4),
    VERIFIED_ABSENT(5),
    ONLINE(6),
    SICK(7);

    companion object {
        val names by lazy { entries.map { it.code } }
        fun fromCode(code: Int): Presence? = entries.find { it.code == code }
        fun fromString(value: String?): Presence? = value?.let { valueOf(value) }
    }
}