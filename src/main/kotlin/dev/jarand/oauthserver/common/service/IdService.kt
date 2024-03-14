package dev.jarand.oauthserver.common.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class IdService(private val uuidSupplier: () -> UUID) {

    fun generateAccountId(): UUID {
        return uuidSupplier.invoke()
    }

    fun generateAppId(): UUID {
        return uuidSupplier.invoke()
    }

    fun generateClientId(): String {
        return uuidSupplier.invoke().toString().replace("-", "")
    }

    fun generateClientSecret(): String {
        return uuidSupplier.invoke().toString().replace("-", "")
    }
}
