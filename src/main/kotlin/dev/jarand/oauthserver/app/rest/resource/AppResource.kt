package dev.jarand.oauthserver.app.rest.resource

data class AppResource(
    val id: String,
    val name: String,
    val clientId: String,
    val redirectURI: List<String>,
    val owner: String,
    val created: String
)
