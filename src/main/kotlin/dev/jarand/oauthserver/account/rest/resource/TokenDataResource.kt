package dev.jarand.oauthserver.account.rest.resource

data class TokenDataResource(
    val subject: String,
    val authenticationIdentifier: String,
    val authenticationMethod: String,
    val scope: List<String>,
    val expiresIn: Long
)
