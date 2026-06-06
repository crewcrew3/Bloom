package ru.itis.bloom.shared.feature.skindiary.impl.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.impl.data.SkinDiaryRepositoryImpl
import ru.itis.bloom.shared.feature.skindiary.impl.data.network.SkinDiaryApiImpl
import ru.itis.bloom.shared.feature.skindiary.impl.domain.DiaryEventBus
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.DeleteDiaryEntryUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntriesUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntryByIdUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.SaveDiaryEntryUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.SyncPendingDiaryEntriesUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation.DiaryNavigationHandler

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
            tokenStorage = get<TokenStorage>()
        )
    }

    singleOf(::DiaryNavigationHandler)
    // Presentation (MVI)
    viewModelOf(::DiaryListViewModel)
    viewModelOf(::DiaryCreateEditViewModel)
    viewModelOf(::DiaryDetailViewModel)

    single { DiaryEventBus() }

}