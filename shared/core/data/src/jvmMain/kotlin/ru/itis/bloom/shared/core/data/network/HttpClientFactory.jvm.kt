package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint

actual fun createHttpClient(): HttpClient {
    val engine = CIO.create {
        endpoint {
            connectTimeout = 30_000
            requestTimeout = 30_000
        }
    }
    return createCommonHttpClient(engine)
}