package dev.jarand.oauthserver.app

import com.jayway.jsonpath.JsonPath
import dev.jarand.oauthserver.config.ComponentTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

class AppComponentTest : ComponentTest() {

    @Test
    fun `POST app should return 200 and credentials and GET apps should return 200 and expected list of apps`() {
        every { idService.generateAppId() } returns UUID.fromString("857f5761-a4cc-4fec-b55b-5b4a9ca4efb9")
        every { idService.generateClientId() } returns "2be9ee9fbba84afab20653d6b99c18ce"
        every { idService.generateClientSecret() } returns "4fb0bba349cf456d960f26beb654dc03"
        every { timeService.generateAppCreated() } returns Instant.parse("2024-02-15T18:07:34.258615500Z")

        val token = registerAndLoginAndGetToken()

        mockMvc.perform(
            post("/app")
                .header(AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "name": "My OAuth 2.0 Application",
                      "redirectURI": [
                        "http://localhost/oauth/callback"
                      ]
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                      "clientId": "2be9ee9fbba84afab20653d6b99c18ce",
                      "clientSecret": "4fb0bba349cf456d960f26beb654dc03"
                    }
                    """.trimIndent(), true
                )
            )

        mockMvc.perform(
            get("/app")
                .header(AUTHORIZATION, "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    [
                      {
                        "id": "857f5761-a4cc-4fec-b55b-5b4a9ca4efb9",
                        "name": "My OAuth 2.0 Application",
                        "redirectURI": [
                          "http://localhost/oauth/callback"
                        ],
                        "clientId": "2be9ee9fbba84afab20653d6b99c18ce",
                        "owner": "d68c7e5a-f21e-462f-9f7d-30b91c793275",
                        "created": "2024-02-15T18:07:34.258615500Z"
                      }
                    ]
                    """.trimIndent(), true
                )
            )
    }

    @Test
    fun `POST app without authorization should return 403`() {
        mockMvc.perform(
            post("/app")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "name": "My OAuth 2.0 Application",
                      "redirectURI": [
                        "http://localhost/oauth/callback"
                      ]
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `GET apps without authorization should return 401`() {
        mockMvc.perform(
            get("/app")
        )
            .andExpect(status().isUnauthorized)
    }

    private fun registerAndLoginAndGetToken(): String {
        every { idService.generateAccountId() } returns UUID.fromString("d68c7e5a-f21e-462f-9f7d-30b91c793275")
        every { timeService.generateAccountCreated() } returns Instant.now()

        val email = "app-${UUID.randomUUID()}@jarand.dev"
        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "$email",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)

        val loginResource = mockMvc.perform(
            post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "$email",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
        return JsonPath.parse(loginResource).read("$.token")
    }
}
