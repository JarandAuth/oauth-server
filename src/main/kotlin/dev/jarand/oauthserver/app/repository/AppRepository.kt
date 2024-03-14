package dev.jarand.oauthserver.app.repository

import dev.jarand.oauthserver.app.domain.App
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class AppRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun insertApp(app: App) {
        jdbcTemplate.update(
            """
            INSERT INTO app (id, name, client_id, client_secret, owner, created)
            VALUES (:id, :name, :client_id, :client_secret, :owner, :created)
            """.trimIndent(),
            mapOf(
                "id" to app.id,
                "name" to app.name,
                "client_id" to app.clientId,
                "client_secret" to app.clientSecret,
                "owner" to app.owner,
                "created" to app.created.toString()
            )
        )

        jdbcTemplate.batchUpdate(
            """
            INSERT INTO app_redirect_uri (redirect_uri, app_id)
            VALUES  (:redirect_uri, :app_id)
            """.trimIndent(),
            app.redirectURI.map {
                mapOf(
                    "redirect_uri" to it,
                    "app_id" to app.id
                )
            }.toTypedArray()
        )
    }

    fun getAppsByOwner(owner: String): List<App> {
        return jdbcTemplate.query(
            """
            SELECT id, name, client_id, client_secret, owner, created
            FROM app
            WHERE owner = :owner
            """.trimIndent(),
            mapOf("owner" to owner)
        ) { resultSet, _ ->
            val appId = resultSet.getObject("id", UUID::class.java)
            App(
                id = appId,
                name = resultSet.getString("name"),
                clientId = resultSet.getString("client_id"),
                clientSecret = resultSet.getString("client_secret"),
                redirectURI = getRedirectURIs(appId),
                owner = resultSet.getString("owner"),
                created = Instant.parse(resultSet.getString("created"))
            )
        }
    }

    private fun getRedirectURIs(appId: UUID): List<String> {
        return jdbcTemplate.query(
            """
            SELECT redirect_uri
            FROM app_redirect_uri
            WHERE app_id = :app_id
            """.trimIndent(),
            mapOf("app_id" to appId)
        ) { resultSet, _ -> resultSet.getString("redirect_uri") }
    }
}
