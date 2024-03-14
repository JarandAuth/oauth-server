package dev.jarand.oauthserver.account.rest.resource

data class TokenResource(
    val token: String,
    val refreshToken: String
)
