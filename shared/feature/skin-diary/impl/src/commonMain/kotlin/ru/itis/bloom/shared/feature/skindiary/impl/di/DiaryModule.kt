package ru.itis.bloom.shared.feature.skindiary.impl.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.impl.data.SkinDiaryRepositoryImpl
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi.DiaryListViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation.DiaryNavigationHandler
import ru.itis.bloom.shared.feature.skindiary.impl.data.network.SkinDiaryApiImpl
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.DeleteDiaryEntryUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntriesUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntryByIdUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.SaveDiaryEntryUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.SyncPendingDiaryEntriesUseCase

fun diaryModule(currentUserIdProvider: () -> String = { "demo-user-id" }): Module = module {

    singleOf(::GetDiaryEntriesUseCase)
    singleOf(::GetDiaryEntryByIdUseCase)
    singleOf(::SaveDiaryEntryUseCase)
    singleOf(::SyncPendingDiaryEntriesUseCase)
    singleOf(::DeleteDiaryEntryUseCase)

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

    singleOf(::DiaryNavigationHandler)
    // Presentation (MVI)
    viewModelOf(::DiaryListViewModel)
}