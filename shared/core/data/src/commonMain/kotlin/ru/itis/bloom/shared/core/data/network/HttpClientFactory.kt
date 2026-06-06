package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient
import ru.itis.bloom.shared.core.data.network.token.TokenStorage

expect fun createHttpClient(tokenStorage: TokenStorage): HttpClient