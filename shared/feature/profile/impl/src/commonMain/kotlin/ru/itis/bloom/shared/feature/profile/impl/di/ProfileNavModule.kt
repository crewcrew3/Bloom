package ru.itis.bloom.shared.feature.profile.impl.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.profile.impl.navigation.profileEntryBuilder

typealias EntryBuilder = EntryProviderScope<NavKey>.() -> Unit

val profileNavModule = module {
    factory<EntryBuilder> {
        EntryProviderScope<NavKey>::profileEntryBuilder
    }
}