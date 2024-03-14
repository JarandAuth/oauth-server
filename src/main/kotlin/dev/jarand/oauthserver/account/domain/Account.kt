package dev.jarand.oauthserver.account.domain

import java.time.Instant
import java.util.*

data class Account(
    val id: UUID,
    val email: String,
    val password: String,
    val created: Instant
)
