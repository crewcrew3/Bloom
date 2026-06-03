package ru.itis.bloom.shared.core.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase

expect val platformModule: Module

val commonDatabaseModule = module {
    single { AppDatabase(get()) }
}