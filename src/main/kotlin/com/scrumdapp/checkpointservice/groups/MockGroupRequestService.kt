package com.scrumdapp.checkpointservice.groups

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
@ConditionalOnBooleanProperty(name=["USE_MOCK_SERVICE"], havingValue = true)
class MockGroupRequestService: GroupRequestService {
    val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Using mock service")
    }

    override fun getGroupUserIds(groupId: Long): List<Long> {
        return listOf(1, 69, 23, 4)
    }

}