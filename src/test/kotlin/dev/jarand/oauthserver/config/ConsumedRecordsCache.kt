package dev.jarand.oauthserver.config

import com.jayway.jsonpath.JsonPath
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.awaitility.Awaitility
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

@Component
class ConsumedRecordsCache {

    private val records: MutableMap<String, MutableMap<String, List<String>>> = mutableMapOf()

    fun addRecord(record: ConsumerRecord<String, String>) {
        val topic = record.topic()
        val json = record.value()
        val correlationId = JsonPath.parse(json).read<String>("$.metadata.correlationId")

        val recordsByTopic = records.getOrDefault(topic, mapOf()).toMutableMap()
        val recordsByCorrelationId = recordsByTopic.getOrDefault(correlationId, listOf()).toMutableList()

        recordsByCorrelationId.addLast(json)

        recordsByTopic[correlationId] = recordsByCorrelationId
        records[topic] = recordsByTopic
    }

    fun findRecords(correlationId: String, topic: String, numberOfRecords: Int): List<String> {
        val matchingRecords = AtomicReference<List<String>>()
        Awaitility.await()
            .atMost(Duration.ofSeconds(10))
            .pollInterval(Duration.ofMillis(100))
            .until {
                val recordsByTopic = records.getOrDefault(topic, mutableMapOf())
                val recordsByCorrelationId = recordsByTopic.getOrDefault(correlationId, mutableListOf())
                matchingRecords.set(recordsByCorrelationId)
                matchingRecords.get().size == numberOfRecords
            }
        return matchingRecords.get()
    }
}
