package com.scrumdapp.checkpointservice.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.KafkaMessageHeaderAccessor
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class KafkaService(
    val template: KafkaTemplate<String, String>
) {

    fun send(data: Any): CompletableFuture<*> {
        return send(data, headers = MessageHeaderAccessor())
    }

    fun send(data: Any, headers: MessageHeaderAccessor): CompletableFuture<SendResult<String, String>> {
        val topic = data.javaClass.getAnnotation(KafkaMessageTopic::class.java).default
        headers.setHeader(KafkaHeaders.TOPIC, topic)
        headers.setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
        val message = MessageBuilder
            .withPayload(data)
            .setHeaders(headers)
            .build()

        val result = template.send(message)
        return result
    }

}