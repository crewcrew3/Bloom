package ru.itis.bloom.shared.feature.makeupbag.impl.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.makeupbag.impl.navigation.makeupBagEntryBuilder

typealias EntryBuilder = EntryProviderScope<NavKey>.() -> Unit

val makeupBagNavModule = module {
    factory<EntryBuilder> {
        EntryProviderScope<NavKey>::makeupBagEntryBuilder
    }
}