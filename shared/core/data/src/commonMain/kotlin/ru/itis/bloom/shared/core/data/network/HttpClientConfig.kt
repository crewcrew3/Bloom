package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.itis.bloom.shared.core.data.BuildKonfig
import ru.itis.bloom.shared.core.data.network.token.TokenStorage

fun createCommonHttpClient(
    engine: HttpClientEngine,
    tokenStorage: TokenStorage
): HttpClient {
    val logger = createAppLogger()
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                isLenient = true
            })
        }

        install(Logging) {
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    logger.d("BLOOM_KTOR", message)
                }
            }
            level = if (BuildKonfig.IS_DEBUG) LogLevel.ALL else LogLevel.INFO
            sanitizeHeader { header -> header.equals("Authorization", ignoreCase = true) }
        }
        install(AuthPlugin) {
            this.tokenStorage = tokenStorage
        }
        install(HttpRequestRetry) {
            retryIf { _, response -> response.status == HttpStatusCode.Unauthorized }
            maxRetries = 1 // Повторяем только 1 раз (достаточно для рефреша)
            delayMillis { retryCount -> retryCount * 100L } // Небольшая задержка
            // requestPipeline запустится снова при повторе → подставит НОВЫЙ токен
        }
        install(DefaultRequest) {
            url(BuildKonfig.API_BASE_URL)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
    }
}