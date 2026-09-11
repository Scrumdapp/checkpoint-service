package com.scrumdapp.checkpointservice.groups

interface GroupRequestService {
    fun getGroupUserIds(groupId: Long): List<Long>
}