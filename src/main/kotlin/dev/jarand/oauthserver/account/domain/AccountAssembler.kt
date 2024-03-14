package dev.jarand.oauthserver.account.domain

import dev.jarand.oauthserver.account.rest.resource.RegisterResource
import dev.jarand.oauthserver.common.service.IdService
import dev.jarand.oauthserver.common.service.TimeService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AccountAssembler(
    private val passwordEncoder: PasswordEncoder,
    private val idService: IdService,
    private val timeService: TimeService
) {
    fun assemble(resource: RegisterResource): Account {
        return Account(
            id = idService.generateAccountId(),
            email = resource.email,
            password = passwordEncoder.encode(resource.password),
            created = timeService.generateAccountCreated()

        )
    }
}
