package dev.jarand.oauthserver.common.kafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {

    @Bean
    fun rawEventsTopic(
        @Value("\${raw-events.topic-name}") topicName: String,
        @Value("\${raw-events.partitions}") partitions: Int,
        @Value("\${raw-events.replicas}") replicas: Int
    ): NewTopic {
        return TopicBuilder.name(topicName).partitions(partitions).replicas(replicas).build()
    }
}
