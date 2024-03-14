package dev.jarand.oauthserver.context

import dev.jarand.oauthserver.config.ComponentTest
import org.junit.jupiter.api.Test

class ContextComponentTest : ComponentTest() {

    @Test
    fun contextLoads() {
        println("It loads :D")
    }
}
