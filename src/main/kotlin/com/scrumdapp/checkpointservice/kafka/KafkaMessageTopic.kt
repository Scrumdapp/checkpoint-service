package com.scrumdapp.checkpointservice.kafka

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KafkaMessageTopic(
    val default: String
)
