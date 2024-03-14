package dev.jarand.oauthserver.common.security

import dev.jarand.oauthserver.token.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity, tokenService: TokenService): SecurityFilterChain {
        http.httpBasic(Customizer.withDefaults())
            .csrf {
                it.disable()
            }
            .cors {
                it.disable()
            }
            .authorizeHttpRequests { authorizer ->
                authorizer
                    .requestMatchers(HttpMethod.POST, "/account/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/account/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/account/refresh-token").permitAll()
                    .requestMatchers(HttpMethod.GET, "/account/me").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(AuthorizationHeaderFilter(tokenService = tokenService), BasicAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
