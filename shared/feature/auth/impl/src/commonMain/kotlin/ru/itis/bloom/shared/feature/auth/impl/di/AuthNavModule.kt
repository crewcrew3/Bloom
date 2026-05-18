package ru.itis.bloom.shared.feature.auth.impl.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.auth.impl.navigation.authEntryBuilder

// Тип для builder-функции
typealias EntryBuilder = EntryProviderScope<NavKey>.() -> Unit

val authNavModule = module {
    // Регистрируем builder как factory, чтобы composeApp мог его получить
    factory<EntryBuilder> {
        EntryProviderScope<NavKey>::authEntryBuilder
    }
}