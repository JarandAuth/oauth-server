package dev.jarand.oauthserver.token

import dev.jarand.oauthserver.account.domain.Account
import dev.jarand.oauthserver.token.domain.TokenData
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.PrivateKey
import java.security.PublicKey
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class TokenService(
    private val publicKey: PublicKey,
    private val privateKey: PrivateKey
) {

    fun createTokenForDeveloper(account: Account): String {
        logger.info("Creating token for developer account with id: ${account.id} and email: ${account.email}")
        val issuedAt = Instant.now()
        val expiration = issuedAt.plusSeconds(60)

        val signedToken = Jwts.builder()
            .subject(account.id.toString())
            .claim("authenticationIdentifier", account.email)
            .claim("authenticationMethod", "PASSWORD")
            .claim("scope", "developer")
            .id(UUID.randomUUID().toString())
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiration))
            .signWith(privateKey)
            .compact()
        logger.info("Created token for developer account with id: ${account.id} and email: ${account.email}")
        return signedToken
    }

    fun createTokenFromRefreshToken(tokenData: TokenData): String {
        logger.info("Creating token from refresh token for subject: ${tokenData.subject}")
        val issuedAt = Instant.now()
        val expiration = issuedAt.plusSeconds(60)

        val signedToken = Jwts.builder()
            .subject(tokenData.subject)
            .claim("authenticationIdentifier", tokenData.id)
            .claim("authenticationMethod", "REFRESH_TOKEN")
            .claim("scope", tokenData.scope.filter { it != "account.refresh-token" }.joinToString(" "))
            .id(UUID.randomUUID().toString())
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiration))
            .signWith(privateKey)
            .compact()
        logger.info("Created token from refresh token for subject: ${tokenData.subject}")
        return signedToken
    }

    fun createRefreshTokenForDeveloper(account: Account): String {
        logger.info("Creating refresh token for developer account with id: ${account.id} and email: ${account.email}")
        val issuedAt = Instant.now()
        val expiration = issuedAt.plusSeconds(600)

        val signedToken = Jwts.builder()
            .subject(account.id.toString())
            .claim("authenticationIdentifier", account.email)
            .claim("authenticationMethod", "PASSWORD")
            .claim("scope", "account.refresh-token developer")
            .id(UUID.randomUUID().toString())
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiration))
            .signWith(privateKey)
            .compact()
        logger.info("Created refresh token for developer account with id: ${account.id} and email: ${account.email}")
        return signedToken
    }

    fun parseToken(signedToken: String): TokenData? {
        logger.info("Parsing token")

        val claims: Jws<Claims>

        try {
            claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(signedToken)
        } catch (ex: MalformedJwtException) {
            logger.info("Failed to parse token because the token is malformed")
            return null
        } catch (ex: ExpiredJwtException) {
            logger.info("Failed to parse token because the token has expired")
            return null
        } catch (ex: SignatureException) {
            logger.info("Failed to parse token because the token signature was invalid")
            return null
        }

        val subject = claims.payload.subject
        val authenticationIdentifier = claims.payload.get("authenticationIdentifier", String::class.java)
        val authenticationMethod = claims.payload.get("authenticationMethod", String::class.java)
        val scope = claims.payload.get("scope", String::class.java)
        val id = claims.payload.id
        val expiresIn = Duration.between(Instant.now(), claims.payload.expiration.toInstant()).toSeconds()
        logger.info("Parsed token with subject: $subject and scope: $scope (authenticationIdentifier: $authenticationIdentifier, authenticationMethod: $authenticationMethod)")
        return TokenData(
            subject = subject,
            authenticationIdentifier = authenticationIdentifier,
            authenticationMethod = authenticationMethod,
            scope = scope.split(" "),
            id = id,
            expiresIn = expiresIn
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TokenService::class.java)
    }
}
