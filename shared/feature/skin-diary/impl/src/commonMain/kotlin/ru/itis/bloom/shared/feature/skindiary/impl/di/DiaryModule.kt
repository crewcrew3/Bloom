package ru.itis.bloom.shared.feature.skindiary.impl.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.impl.data.SkinDiaryRepositoryImpl
import ru.itis.bloom.shared.feature.skindiary.impl.mvi.DiaryListViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.navigation.DiaryNavigationHandler
import ru.itis.bloom.shared.feature.skindiary.impl.network.SkinDiaryApiImpl

fun diaryModule(currentUserIdProvider: () -> String = { "demo-user-id" }): Module = module {
    // Network
    single<SkinDiaryApi> { SkinDiaryApiImpl(get<HttpClient>()) }
    // Data
    single<SkinDiaryRepository> {
        SkinDiaryRepositoryImpl(
            api = get(),
            database = get<AppDatabase>(),
            currentUserIdProvider = currentUserIdProvider
        )
    }

    single<DiaryNavigationHandler> { DiaryNavigationHandler(get()) }
    // Presentation (MVI)
    viewModelOf(::DiaryListViewModel)
}