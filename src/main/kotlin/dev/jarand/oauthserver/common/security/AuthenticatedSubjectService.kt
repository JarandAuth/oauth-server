package dev.jarand.oauthserver.common.security

import dev.jarand.oauthserver.token.domain.TokenData
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticatedSubjectService {

    fun getTokenData(): TokenData? {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: return null
        return authentication.principal as TokenData
    }
}
