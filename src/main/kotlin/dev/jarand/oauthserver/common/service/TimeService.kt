package dev.jarand.oauthserver.common.service

import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TimeService(private val instantSupplier: () -> Instant) {

    fun generateAccountCreated(): Instant {
        return instantSupplier.invoke()
    }

    fun generateAppCreated(): Instant {
        return instantSupplier.invoke()
    }
}
