package dev.jarand.oauthserver.key.config

import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

@Configuration
class KeyConfig {

    @Bean
    fun publicKey(keyPair: KeyPair): PublicKey {
        return keyPair.public
    }

    @Bean
    fun privateKey(keyPair: KeyPair): PrivateKey {
        return keyPair.private
    }

    @Bean
    fun keyPair(): KeyPair {
        val signatureAlgorithm = Jwts.SIG.ES512
        return signatureAlgorithm.keyPair().build()
    }
}
