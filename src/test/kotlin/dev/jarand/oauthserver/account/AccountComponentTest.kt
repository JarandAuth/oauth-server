package dev.jarand.oauthserver.account

import com.jayway.jsonpath.JsonPath
import dev.jarand.oauthserver.config.ComponentTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

class AccountComponentTest : ComponentTest() {

    @Test
    fun `POST register should return 200`() {
        every { idService.generateAccountId() } returns UUID.fromString("971b29b4-adc1-47df-b547-1be22b31d9dd")
        every { timeService.generateAccountCreated() } returns Instant.now()

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "test@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `POST register twice with the same credentials should return 200 and then 400`() {
        every { idService.generateAccountId() } returns UUID.fromString("bf445ca0-c2ce-4a80-873f-d4c563fc80df")
        every { timeService.generateAccountCreated() } returns Instant.now()

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "duplicate@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "duplicate@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST register and login should return 200`() {
        every { idService.generateAccountId() } returns UUID.fromString("4131d11d-1e4c-4d92-bf91-97ef657e25ae")
        every { timeService.generateAccountCreated() } returns Instant.now()

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "login@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "login@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `POST register and login with wrong email and password should return 401`() {
        every { idService.generateAccountId() } returns UUID.fromString("398039c1-e157-40c4-ac15-0b7551635292")
        every { timeService.generateAccountCreated() } returns Instant.now()

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "correctEmail@jarand.dev",
                        "password": "correctSecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "wrongEmail@jarand.dev",
                        "password": "wrongSecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "wrongEmail@jarand.dev",
                        "password": "correctSecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "correctEmail@jarand.dev",
                        "password": "wrongSecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `POST register and login and GET me should return 200`() {
        every { idService.generateAccountId() } returns UUID.fromString("294cb568-eef4-481e-b7b4-11a1c72c11f9")
        every { timeService.generateAccountCreated() } returns Instant.now()

        mockMvc.perform(
            post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "email": "developer@jarand.dev",
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
                        "email": "developer@jarand.dev",
                        "password": "verySecret"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString
        val token = JsonPath.parse(loginResource).read<String>("$.token")

        mockMvc.perform(
            get("/account/me")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.subject").value("294cb568-eef4-481e-b7b4-11a1c72c11f9"))
            .andExpect(jsonPath("$.authenticationIdentifier").value("developer@jarand.dev"))
            .andExpect(jsonPath("$.authenticationMethod").value("PASSWORD"))
            .andExpect(jsonPath("$.scope").value("developer"))
            .andExpect(jsonPath("$.expiresIn").isNumber)
    }
}
