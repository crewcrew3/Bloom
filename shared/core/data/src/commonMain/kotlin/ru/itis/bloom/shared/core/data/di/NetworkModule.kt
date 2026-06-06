package ru.itis.bloom.shared.core.data.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.network.createHttpClient

val networkModule = module {
    single<HttpClient> { createHttpClient(get()) }
}