package dev.jarand.oauthserver.app.rest.resource

import dev.jarand.oauthserver.app.domain.App
import org.springframework.stereotype.Component

@Component
class AppResourceAssembler {

    fun assemble(apps: List<App>): List<AppResource> {
        return apps.map { assemble(it) }
    }

    private fun assemble(app: App): AppResource {
        return AppResource(
            id = app.id.toString(),
            name = app.name,
            clientId = app.clientId,
            redirectURI = app.redirectURI,
            owner = app.owner,
            created = app.created.toString()
        )
    }
}
