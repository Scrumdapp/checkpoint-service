package com.scrumdapp.checkpointservice.groups

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Primary
@Profile("use_mock_service")
class MockGroupRequestService: GroupRequestService {
    val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Using mock service")
    }

    override fun getGroupUsers(groupId: Long): List<GroupUserResponse> {
        return listOf(
            GroupUserResponse(1, groupId, "Garry", "Godspeed", false),
            GroupUserResponse(2, groupId, "Larry", "Snailspeed", false),
            GroupUserResponse(3, groupId, "Henk", "Hank", true),
            GroupUserResponse(4, groupId, "Harry", "Regularspeed", false)
        )
    }
}