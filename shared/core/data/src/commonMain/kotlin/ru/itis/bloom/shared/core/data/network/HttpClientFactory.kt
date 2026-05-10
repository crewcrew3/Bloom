package ru.itis.bloom.shared.core.data.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient