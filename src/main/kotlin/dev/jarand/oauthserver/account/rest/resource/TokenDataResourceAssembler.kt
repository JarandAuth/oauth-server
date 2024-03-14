package dev.jarand.oauthserver.account.rest.resource

import dev.jarand.oauthserver.token.domain.TokenData
import org.springframework.stereotype.Component

@Component
class TokenDataResourceAssembler {

    fun assemble(tokenData: TokenData): TokenDataResource {
        return TokenDataResource(
            subject = tokenData.subject,
            authenticationIdentifier = tokenData.authenticationIdentifier,
            authenticationMethod = tokenData.authenticationMethod,
            scope = tokenData.scope,
            expiresIn = tokenData.expiresIn
        )
    }
}
