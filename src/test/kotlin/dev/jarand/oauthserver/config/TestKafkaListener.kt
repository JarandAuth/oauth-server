package dev.jarand.oauthserver.config

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class TestKafkaListener(
    private val consumedRecordsCache: ConsumedRecordsCache
) {

    @KafkaListener(
        topics = [
            "\${raw-events.topic-name}"
        ],
        groupId = "TestKafkaListener"
    )
    fun listen(record: ConsumerRecord<String, String>, acknowledgement: Acknowledgment) {
        consumedRecordsCache.addRecord(record)
        acknowledgement.acknowledge()
    }
}
