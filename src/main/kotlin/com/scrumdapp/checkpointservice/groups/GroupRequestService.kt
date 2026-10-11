package com.scrumdapp.checkpointservice.groups

interface GroupRequestService {
    fun getGroupUsers(groupId: Long): List<GroupUserResponse>
}