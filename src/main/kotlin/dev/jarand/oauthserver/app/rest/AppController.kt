package dev.jarand.oauthserver.app.rest

import dev.jarand.oauthserver.app.AppService
import dev.jarand.oauthserver.app.domain.AppAssembler
import dev.jarand.oauthserver.app.rest.resource.*
import dev.jarand.oauthserver.common.security.AuthenticatedSubjectService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("app")
class AppController(
    private val appService: AppService,
    private val appAssembler: AppAssembler,
    private val authenticatedSubjectService: AuthenticatedSubjectService,
    private val appResourceAssembler: AppResourceAssembler,
    private val appCredentialsResourceAssembler: AppCredentialsResourceAssembler
) {

    @PostMapping
    fun createApp(@Valid @RequestBody resource: RegisterAppResource): ResponseEntity<AppCredentialsResource> {
        val authenticatedTokenData = authenticatedSubjectService.getTokenData()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val unsecuredApp = appAssembler.assembleUnsecured(resource, authenticatedTokenData.subject)
        val app = appAssembler.secureApp(unsecuredApp)
        appService.createApp(app)
        return ResponseEntity.ok(appCredentialsResourceAssembler.assemble(unsecuredApp))
    }

    @GetMapping
    fun getApps(): ResponseEntity<List<AppResource>> {
        val authenticatedTokenData = authenticatedSubjectService.getTokenData()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val apps = appService.getAppsByOwner(authenticatedTokenData.subject)
        return ResponseEntity.ok(appResourceAssembler.assemble(apps))
    }
}
