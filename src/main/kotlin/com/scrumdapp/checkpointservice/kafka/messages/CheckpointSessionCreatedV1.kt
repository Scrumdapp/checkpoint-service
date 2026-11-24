package com.scrumdapp.checkpointservice.kafka.messages

import com.scrumdapp.checkpointservice.kafka.KafkaMessageTopic

@KafkaMessageTopic("com.scrumdapp.checkpoints.sessions.created.v1")
data class CheckpointSessionCreatedV1(
    val groupId: Long,
    val sessionId: Long
)