package dev.jarand.oauthserver.common.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterConfig
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun forwardedHeaderLoggingFilter(): FilterRegistrationBean<Filter> {
        return FilterRegistrationBean(Filter { servletRequest, servetResponse, chain ->
            val request = servletRequest as HttpServletRequest
            val response = servetResponse as HttpServletResponse

            val uri = request.requestURI

            val forwardedFor = request.getHeader("X-Forwarded-For")
            val forwardedProtocol = request.getHeader("X-Forwarded-Proto")
            val forwardedHost = request.getHeader("X-Forwarded-Host")
            val forwardedPort = request.getHeader("X-Forwarded-Port")
            val forwardedPrefix = request.getHeader("X-Forwarded-Prefix")

            logger.info("Request to URI: $uri [X-forwarded-For: $forwardedFor] [X-forwarded-Proto: $forwardedProtocol] [X-forwarded-Host: $forwardedHost] [X-forwarded-Port: $forwardedPort] [X-forwarded-Prefix: $forwardedPrefix]")

            chain.doFilter(request, response)
        })
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FilterConfig::class.java)
    }
}
