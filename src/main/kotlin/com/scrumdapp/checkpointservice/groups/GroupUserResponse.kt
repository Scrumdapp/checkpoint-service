package com.scrumdapp.checkpointservice.groups

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupUserResponse(
    val user_id: Long,
    val group_id: Long,
    val first_name: String,
    val last_name: String,
    var is_ghost: Boolean,
)