package dev.jarand.oauthserver.config

import dev.jarand.oauthserver.common.service.IdService
import dev.jarand.oauthserver.common.service.TimeService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ComponentTestConfig {

    @Bean
    fun idService(): IdService {
        return mockk()
    }

    @Bean
    fun timeService(): TimeService {
        return mockk()
    }
}
