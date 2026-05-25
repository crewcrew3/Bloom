package ru.itis.bloom.shared.core.data.di

import org.koin.core.qualifier.named
import org.koin.dsl.module

internal object QualifierDBName
internal object QualifierSettingsName
internal object QualifierApiBaseUrl

val qualifierModule = module {
    factory<String>(named<QualifierDBName>()) { "bloom.db" }
    factory<String>(named<QualifierSettingsName>()) { "bloom_preferences" }

    // TODO BuildKonfig, заменить на: get<BuildKonfig>().BASE_URL
    factory<String>(named<QualifierApiBaseUrl>()) { "http://localhost:8080/v1" }
}