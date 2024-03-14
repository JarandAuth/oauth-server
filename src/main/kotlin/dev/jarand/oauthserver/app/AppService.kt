package dev.jarand.oauthserver.app

import dev.jarand.oauthserver.app.domain.App
import dev.jarand.oauthserver.app.repository.AppRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AppService(
    private val appRepository: AppRepository
) {

    fun createApp(app: App) {
        logger.debug("Creating app with id: ${app.id}")
        appRepository.insertApp(app)
        logger.info("Created app with id: ${app.id}")
    }

    fun getAppsByOwner(owner: String): List<App> {
        return appRepository.getAppsByOwner(owner)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AppService::class.java)
    }
}
