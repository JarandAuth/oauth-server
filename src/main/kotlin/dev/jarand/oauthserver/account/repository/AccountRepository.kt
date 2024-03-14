package dev.jarand.oauthserver.account.repository

import dev.jarand.oauthserver.account.domain.Account
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class AccountRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun insertAccount(account: Account) {
        jdbcTemplate.update(
            """
            INSERT INTO account (id, email, password, created)
            VALUES (:id, :email, :password, :created)
            """.trimIndent(),
            mapOf(
                "id" to account.id,
                "email" to account.email,
                "password" to account.password,
                "created" to account.created.toString()
            )
        )
    }

    fun getAccount(email: String): Account? {
        try {
            return jdbcTemplate.queryForObject(
                """
                SELECT id, email, password, created
                FROM account
                WHERE email = :email
                """.trimIndent(),
                mapOf("email" to email)
            ) { resultSet, _ ->
                Account(
                    id = resultSet.getObject("id", UUID::class.java),
                    email = resultSet.getString("email"),
                    password = resultSet.getString("password"),
                    created = Instant.parse(resultSet.getString("created"))
                )
            }
        } catch (ex: EmptyResultDataAccessException) {
            return null
        }
    }

    fun emailExists(email: String): Boolean {
        return jdbcTemplate.queryForObject(
            """
            SELECT EXISTS (SELECT 1 FROM account WHERE email = :email)
            """.trimIndent(),
            mapOf("email" to email),
            Boolean::class.java
        )!!
    }
}
