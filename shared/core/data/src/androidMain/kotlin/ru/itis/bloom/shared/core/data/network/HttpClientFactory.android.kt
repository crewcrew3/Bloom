package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import ru.itis.bloom.shared.core.data.network.token.AndroidTokenStorage
import java.util.concurrent.TimeUnit

actual fun createHttpClient(tokenStorage: AndroidTokenStorage): HttpClient {
    val engine = OkHttp.create {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }
    }
    return createCommonHttpClient(engine,tokenStorage)
}