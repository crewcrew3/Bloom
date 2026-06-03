package ru.itis.bloom.shared.core.data.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.core.data.network.token.AndroidTokenStorage
import ru.itis.bloom.shared.core.data.network.token.TokenStorage

actual val platformModule: Module = module {
    single<AndroidSqliteDriver> {
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = get<Context>(),
            name = get(named<QualifierDBName>())
        )
    }
    single<SqlDriver> { get<AndroidSqliteDriver>() }
    single<TokenStorage> {
        AndroidTokenStorage(androidContext())
    }
}