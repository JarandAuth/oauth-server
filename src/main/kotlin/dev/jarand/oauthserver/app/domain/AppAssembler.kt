package dev.jarand.oauthserver.app.domain

import dev.jarand.oauthserver.app.rest.resource.RegisterAppResource
import dev.jarand.oauthserver.common.service.IdService
import dev.jarand.oauthserver.common.service.TimeService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AppAssembler(
    private val passwordEncoder: PasswordEncoder,
    private val idService: IdService,
    private val timeService: TimeService
) {

    fun assembleUnsecured(resource: RegisterAppResource, owner: String): App {
        return App(
            id = idService.generateAppId(),
            name = resource.name,
            clientId = idService.generateClientId(),
            clientSecret = idService.generateClientSecret(),
            redirectURI = resource.redirectURI,
            owner = owner,
            created = timeService.generateAppCreated()
        )
    }

    fun secureApp(unsecuredApp: App): App {
        return unsecuredApp.copy(clientSecret = passwordEncoder.encode(unsecuredApp.clientSecret))
    }
}
