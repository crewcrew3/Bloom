package ru.itis.bloom.shared.core.data.di

import org.koin.dsl.module
import io.ktor.client.HttpClient
import ru.itis.bloom.shared.core.data.network.createHttpClient

val networkModule = module {
    single<HttpClient> { createHttpClient() }
}