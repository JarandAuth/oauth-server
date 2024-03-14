package dev.jarand.oauthserver.app.rest.resource

import dev.jarand.oauthserver.app.domain.App
import org.springframework.stereotype.Component

@Component
class AppCredentialsResourceAssembler {

    fun assemble(unsecuredApp: App): AppCredentialsResource {
        return AppCredentialsResource(
            clientId = unsecuredApp.clientId,
            clientSecret = unsecuredApp.clientSecret
        )
    }
}
