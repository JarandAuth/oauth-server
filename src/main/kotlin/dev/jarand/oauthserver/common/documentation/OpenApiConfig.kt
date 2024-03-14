package dev.jarand.oauthserver.common.documentation

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(@Value("\${spring.application.name}") applicationName: String): OpenAPI {
        return OpenAPI().info(Info().title(applicationName))
    }
}
