package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.model.RefreshTokenRequest
import ru.itis.bloom.shared.core.data.network.token.TokenRefresher
import ru.itis.bloom.shared.core.data.network.token.TokenStorage

class AuthPlugin private constructor(
    private val tokenStorage: TokenStorage,
    private val tokenRefresher: TokenRefresher? = null
) {

    class Config {
        var tokenStorage: TokenStorage? = null
        var tokenRefresher: TokenRefresher? = null
    }

    companion object Plugin : HttpClientPlugin<Config, AuthPlugin> {
        override val key: AttributeKey<AuthPlugin> = AttributeKey("AuthPlugin")

        override fun prepare(block: Config.() -> Unit): AuthPlugin {
            val config = Config().apply(block)
            val storage = config.tokenStorage
                ?: error("TokenStorage must be provided in AuthPlugin configuration")
            return AuthPlugin(storage, config.tokenRefresher)
        }

        override fun install(plugin: AuthPlugin, scope: HttpClient) {
            val refreshMutex = Mutex()

            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                val token = plugin.tokenStorage.getAccessToken()
                if (!token.isNullOrEmpty() && !plugin.tokenStorage.isAccessTokenExpired()) {
                    context.headers.append(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                if (subject.status == HttpStatusCode.Unauthorized) {
                    refreshMutex.withLock {
                        if (plugin.tokenStorage.isAccessTokenExpired()) {
                            val refreshToken = plugin.tokenStorage.getRefreshToken()
                            if (refreshToken != null && plugin.tokenRefresher != null) {
                                when (val refreshResult = plugin.tokenRefresher.refreshTokens(
                                    RefreshTokenRequest(refreshToken)
                                )) {
                                    is Result.Success -> {
                                        // Токен уже сохранён внутри refreshTokens()
                                        println("[BLOOM_AUTH_PLUGIN] Token refreshed successfully")
                                    }

                                    is Result.Error -> {
                                        println("[BLOOM_AUTH_PLUGIN] Refresh failed: ${refreshResult.error}")
                                        plugin.tokenStorage.clearTokens()
                                    }

                                    else -> {}
                                }
                            } else {
                                plugin.tokenStorage.clearTokens()
                            }
                        }
                    }
                }
                proceed()
            }
        }
    }
}