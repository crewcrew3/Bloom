package ru.itis.bloom.shared.feature.auth.impl.di


import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.impl.data.AuthRepositoryImpl
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthViewModel
import ru.itis.bloom.shared.feature.auth.impl.navigation.AuthNavigationHandler
import ru.itis.bloom.shared.feature.auth.impl.network.AuthApiImpl


val authModule = module {
    // API
    single<AuthApi> { AuthApiImpl(httpClient = get()) }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(authApi = get()) }

    // ViewModel
    viewModelOf(::AuthViewModel)

    // Navigation
    factory { AuthNavigationHandler(get()) }
}