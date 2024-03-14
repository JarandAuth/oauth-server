package dev.jarand.oauthserver.app.rest.resource

data class RegisterAppResource(
    val name: String,
    val redirectURI: List<String>
)
