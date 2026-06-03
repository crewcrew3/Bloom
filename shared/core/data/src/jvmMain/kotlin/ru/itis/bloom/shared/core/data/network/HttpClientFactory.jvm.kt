package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import ru.itis.bloom.shared.core.data.network.token.TokenStorage

actual fun createHttpClient(tokenStorage: TokenStorage): HttpClient {
    val engine = CIO.create {
        endpoint {
            connectTimeout = 30_000
            requestTimeout = 30_000
        }
    }
    return createCommonHttpClient(engine, tokenStorage)
}