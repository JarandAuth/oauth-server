package dev.jarand.oauthserver.config

import dev.jarand.oauthserver.common.service.IdService
import dev.jarand.oauthserver.common.service.TimeService
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    properties = [
        "spring.main.allow-bean-definition-overriding=true",
        "spring.datasource.url=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:config/h2/init.sql'",
        "kafka.replicas=1"
    ],
    classes = [
        ComponentTestConfig::class
    ]
)
@EmbeddedKafka(
    brokerProperties = [
        "listeners=PLAINTEXT://localhost:9091",
        "port=9091",
        "auto.create.topics.enable=false"
    ]
)
@AutoConfigureMockMvc
class ComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var consumedRecordsCache: ConsumedRecordsCache

    @Autowired
    lateinit var idService: IdService

    @Autowired
    lateinit var timeService: TimeService
}
