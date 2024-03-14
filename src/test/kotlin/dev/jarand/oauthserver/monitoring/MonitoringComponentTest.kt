package dev.jarand.oauthserver.monitoring

import com.jayway.jsonpath.JsonPath
import dev.jarand.oauthserver.config.ComponentTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant
import java.util.*

class MonitoringComponentTest : ComponentTest() {

    @Test
    fun `GET health should return 200 with status UP and expected groups`() {
        mockMvc.perform(
            get("/actuator/health")
                .header(AUTHORIZATION, "Bearer ${registerAndLoginAndGetToken()}}")
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                      "status": "UP",
                      "groups": [
                        "liveness",
                        "readiness"
                      ]
                    }
                    """.trimIndent(), true
                )
            )
    }

    @Test
    fun `GET health without authorization should return 401`() {
        mockMvc.perform(
            get("/actuator/health")
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `GET health liveness should return 200 with status UP and expected groups`() {
        mockMvc.perform(
            get("/actuator/health/liveness")
                .header(AUTHORIZATION, "Bearer ${registerAndLoginAndGetToken()}}")
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                      "status": "UP"
                    }
                    """.trimIndent(), true
                )
            )
    }

    @Test
    fun `GET health liveness without authorization should return 401`() {
        mockMvc.perform(
            get("/actuator/health/liveness")
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `GET health readiness should return 200 with status UP and expected groups`() {
        mockMvc.perform(
            get("/actuator/health/readiness")
                .header(AUTHORIZATION, "Bearer ${registerAndLoginAndGetToken()}}")
        )
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                      "status": "UP"
                    }
                    """.trimIndent(), true
                )
            )
    }

    @Test
    fun `GET health readiness without authorization should return 401`() {
        mockMvc.perform(
            get("/actuator/health/readiness")
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `GET prometheus should return 200 with status UP and expected groups`() {
        mockMvc.perform(
            get("/actuator/prometheus")
                .header(AUTHORIZATION, "Bearer ${registerAndLoginAndGetToken()}}")
        )
            .andExpect(status().isOk)
            .andExpect(header().string(CONTENT_TYPE, "text/plain;version=0.0.4;charset=utf-8"))
    }

    @Test
    fun `GET prometheus without authorization should return 401`() {
        mockMvc.perform(
            get("/actuator/prometheus")
        )
            .andExpect(status().isUnauthorized)
    }

    private fun registerAndLoginAndGetToken(): String {
        every { idService.generateAccountId() } returns UUID.randomUUID()
        every { timeService.generateAccountCreated() } returns Instant.now()

        val email = "monitoring-${UUID.randomUUID()}@jarand.dev"
        mockMvc.perform(
            MockMvcRequestBuilders.post("/account/register")
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
            MockMvcRequestBuilders.post("/account/login")
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
