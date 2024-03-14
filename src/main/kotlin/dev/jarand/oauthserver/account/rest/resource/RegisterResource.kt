package dev.jarand.oauthserver.account.rest.resource

import jakarta.validation.constraints.NotEmpty

data class RegisterResource(
    @field:NotEmpty
    val email: String,
    @field:NotEmpty
    val password: String
)
