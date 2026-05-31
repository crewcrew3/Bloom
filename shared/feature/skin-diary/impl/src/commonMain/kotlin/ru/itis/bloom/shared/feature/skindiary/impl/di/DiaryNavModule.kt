package ru.itis.bloom.shared.feature.skindiary.impl.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation.diaryEntryBuilder

typealias EntryBuilder = EntryProviderScope<NavKey>.() -> Unit

val diaryNavModule = module {
    factory<EntryBuilder> {
        EntryProviderScope<NavKey>::diaryEntryBuilder
    }
}