package dev.jarand.oauthserver.account.rest

import dev.jarand.oauthserver.account.AccountService
import dev.jarand.oauthserver.account.domain.AccountAssembler
import dev.jarand.oauthserver.account.rest.resource.*
import dev.jarand.oauthserver.account.validator.AccountValidator
import dev.jarand.oauthserver.common.validator.domain.ValidationResult
import dev.jarand.oauthserver.token.TokenService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("account")
class AccountController(
    private val accountService: AccountService,
    private val accountAssembler: AccountAssembler,
    private val accountValidator: AccountValidator,
    private val tokenService: TokenService,
    private val tokenDataResourceAssembler: TokenDataResourceAssembler
) {

    @PostMapping("register")
    fun register(@Valid @RequestBody resource: RegisterResource): ResponseEntity<Any> {
        val account = accountAssembler.assemble(resource = resource)
        val validation = accountValidator.validateRegistration(account = account)
        if (validation != ValidationResult.SUCCESS) {
            return ResponseEntity.badRequest().build()
        }
        accountService.createAccount(account = account)
        return ResponseEntity.ok().build()
    }

    @PostMapping("login")
    fun login(@Valid @RequestBody resource: LoginResource): ResponseEntity<TokenResource> {
        val account = accountService.getAccount(email = resource.email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val validation = accountValidator.validateLogin(resource = resource, account = account)
        if (validation != ValidationResult.SUCCESS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val signedToken = tokenService.createTokenForDeveloper(account = account)
        val signedRefreshToken = tokenService.createRefreshTokenForDeveloper(account = account)
        return ResponseEntity.ok(TokenResource(token = signedToken, refreshToken = signedRefreshToken))
    }

    @PostMapping("refresh-token")
    fun refreshToken(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<TokenResource> {
        val signedRefreshToken = authorizationHeader.replace("Bearer ", "")
        val tokenData = tokenService.parseToken(signedToken = signedRefreshToken)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (!tokenData.scope.contains("account.refresh-token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val signedToken = tokenService.createTokenFromRefreshToken(tokenData = tokenData)
        val refreshedSignedRefreshToken = tokenService.createRefreshTokenFromRefreshToken(tokenData = tokenData)
        return ResponseEntity.ok(TokenResource(token = signedToken, refreshToken = refreshedSignedRefreshToken))
    }

    @GetMapping("me")
    fun me(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<TokenDataResource> {
        val signedToken = authorizationHeader.replace("Bearer ", "")
        val tokenData = tokenService.parseToken(signedToken = signedToken)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return ResponseEntity.ok(tokenDataResourceAssembler.assemble(tokenData = tokenData))
    }
}
