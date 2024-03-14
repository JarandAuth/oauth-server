package dev.jarand.oauthserver.app.domain

import java.time.Instant
import java.util.*

data class App(
    val id: UUID,
    val name: String,
    val clientId: String,
    val clientSecret: String,
    val redirectURI: List<String>,
    val owner: String,
    val created: Instant
)
