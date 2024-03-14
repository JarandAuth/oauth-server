package dev.jarand.oauthserver.account.validator

import dev.jarand.oauthserver.account.AccountService
import dev.jarand.oauthserver.account.domain.Account
import dev.jarand.oauthserver.account.rest.resource.LoginResource
import dev.jarand.oauthserver.common.validator.domain.ValidationResult
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AccountValidator(
    private val accountService: AccountService,
    private val passwordEncoder: PasswordEncoder
) {

    fun validateRegistration(account: Account): ValidationResult {
        logger.info("Validating registration for account with id: ${account.id} and email: ${account.email}")
        if (accountService.emailExists(account.email)) {
            logger.info("Registration validation for account with id: ${account.id} and email: ${account.email} failed due to duplicate email")
            return ValidationResult.DUPLICATE_EMAIL
        }
        logger.info("Registration validation for account with id: ${account.id} and email: ${account.email} was successful")
        return ValidationResult.SUCCESS
    }

    fun validateLogin(resource: LoginResource, account: Account): ValidationResult {
        logger.info("Validating login for account with id: ${account.id} and email: ${account.email}")
        if (!passwordEncoder.matches(resource.password, account.password)) {
            logger.info("Login validation for account with id: ${account.id} and email: ${account.email} failed due to password mismatch")
            return ValidationResult.PASSWORD_MISMATCH
        }
        logger.info("Login validation for account with id: ${account.id} and email: ${account.email} was successful")
        return ValidationResult.SUCCESS
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AccountValidator::class.java)
    }
}
