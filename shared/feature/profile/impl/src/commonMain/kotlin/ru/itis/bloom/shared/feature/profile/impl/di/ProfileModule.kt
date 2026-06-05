package ru.itis.bloom.shared.feature.profile.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.profile.api.ProfileApi
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository
import ru.itis.bloom.shared.feature.profile.impl.data.ProfileRepositoryImpl
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.GetProfileUseCase
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileViewModel
import ru.itis.bloom.shared.feature.profile.impl.navigation.ProfileNavigationHandler
import ru.itis.bloom.shared.feature.profile.impl.network.ProfileApiImpl

val profileModule = module {
    singleOf(::ProfileApiImpl) bind ProfileApi::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    singleOf(::GetProfileUseCase)

    singleOf(::ProfileNavigationHandler)

    viewModelOf(::ProfileViewModel)
}