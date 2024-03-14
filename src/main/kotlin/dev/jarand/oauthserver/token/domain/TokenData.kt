package dev.jarand.oauthserver.token.domain

data class TokenData(
    val subject: String,
    val authenticationIdentifier: String,
    val authenticationMethod: String,
    val scope: List<String>,
    val id: String,
    val expiresIn: Long
)
