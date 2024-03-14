package dev.jarand.oauthserver.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.util.*

@Configuration
class Config {

    @Bean
    fun uuidSupplier(): () -> UUID {
        return { UUID.randomUUID() }
    }

    @Bean
    fun instantSupplier(): () -> Instant {
        return { Instant.now() }
    }
}
