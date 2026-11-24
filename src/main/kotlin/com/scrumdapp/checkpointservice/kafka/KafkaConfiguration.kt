package com.scrumdapp.checkpointservice.kafka

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.JacksonJsonMessageConverter
import org.springframework.kafka.support.serializer.JacksonJsonSerializer

@Configuration
class KafkaConfiguration {

    @Bean
    fun producerFactory(kafkaProperties: KafkaProperties): ProducerFactory<String, String> {
        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties): ConsumerFactory<String, String> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            StringDeserializer()
        )
    }

    @Bean
    fun kafkaTemplateFactory(
        producerFactory: ProducerFactory<String, String>,
//        consumerFactory: ConsumerFactory<String, String>
    ): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory).apply {
//            setConsumerFactory(consumerFactory)
//            setMessagingConverter(org.springframework.messaging.converter.JacksonJsonMessageConverter())
        }
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            setConsumerFactory(consumerFactory)
            setRecordMessageConverter(JacksonJsonMessageConverter())
        }
    }
}