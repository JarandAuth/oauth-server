package dev.jarand.oauthserver.account

import dev.jarand.oauthserver.account.domain.Account
import dev.jarand.oauthserver.account.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun createAccount(account: Account) {
        logger.info("Creating account with id: ${account.id}")
        accountRepository.insertAccount(account = account)
        logger.info("Created account with id: ${account.id}")
    }

    fun getAccount(email: String): Account? {
        logger.info("Getting account with email: $email")
        val account = accountRepository.getAccount(email = email)
        if (account == null) {
            logger.info("Could not find account with email: $email")
            return null
        }
        logger.info("Got account with email: $email (id: ${account.id})")
        return account
    }

    fun emailExists(email: String): Boolean {
        logger.info("Checking if email: $email exists")
        val emailExists = accountRepository.emailExists(email = email)
        if (!emailExists) {
            logger.info("Check for existing email: $email returned false")
            return false
        }
        logger.info("Check for existing email: $email returned true")
        return true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AccountService::class.java)
    }
}
