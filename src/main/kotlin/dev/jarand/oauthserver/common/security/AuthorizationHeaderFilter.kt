package dev.jarand.oauthserver.common.security

import dev.jarand.oauthserver.token.TokenService
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class AuthorizationHeaderFilter(private val tokenService: TokenService) : Filter {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        logger.info("Authorization header filter started")

        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader == null) {
            logger.info("Authorization header filter passing request to the chain as there was no authorization header")
            chain.doFilter(request, response)
            return
        }

        val signedToken = authorizationHeader.replace("Bearer ", "")

        logger.info("Authorization header filter retrieved token from header")

        val tokenData = tokenService.parseToken(signedToken = signedToken)

        if (tokenData == null) {
            logger.info("Authorization header filter responded with 401 because it failed to parse token")
            response.sendError(HttpStatus.UNAUTHORIZED.value())
            return
        }

        logger.info("Authorization header filter parsed token for subject: ${tokenData.subject}")

        val authentication = UsernamePasswordAuthenticationToken(tokenData, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication

        logger.info("Authorization header filter set authentication in security context")

        chain.doFilter(request, response)

        logger.info("Authorization header filter completed")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderFilter::class.java)
    }
}
