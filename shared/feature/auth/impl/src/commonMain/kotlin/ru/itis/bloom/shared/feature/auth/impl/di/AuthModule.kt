package ru.itis.bloom.shared.feature.auth.impl.di


import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.impl.data.AuthRepositoryImpl
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.ConfirmResetPasswordUseCase
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.LoadProfileAndSaveUserIdUseCase
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.LoginUseCase
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.RegisterUseCase
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.ResetPasswordUseCase
import ru.itis.bloom.shared.feature.auth.impl.domain.usecase.VerifyEmailUseCase
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthViewModel
import ru.itis.bloom.shared.feature.auth.impl.navigation.AuthNavigationHandler
import ru.itis.bloom.shared.feature.auth.impl.network.AuthApiImpl


val authModule = module {
    // API
    single<AuthApi> {
        AuthApiImpl(
            httpClient = get(),
            tokenStorage = get()
        )
    }

    //UseCase
    single { LoginUseCase(repository = get()) }
    single { RegisterUseCase(repository = get()) }
    single { VerifyEmailUseCase(repository = get()) }
    single { ResetPasswordUseCase(repository = get()) }
    single { ConfirmResetPasswordUseCase(repository = get()) }
    single { LoadProfileAndSaveUserIdUseCase(get(), get()) }
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(authApi = get()) }

    // ViewModel
    viewModelOf(::AuthViewModel)

    // Navigation
    factory { AuthNavigationHandler(get()) }
}