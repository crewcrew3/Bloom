package ru.itis.bloom.shared.core.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.core.data.network.token.DesktopTokenStorage
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import java.io.File

actual val platformModule: Module = module {
    single<SqlDriver> {
        val dbName = get<String>(named<QualifierDBName>())
        val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)

        if (!dbFile.exists()) {
            JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}").use { driver ->
                AppDatabase.Schema.create(driver)
            }
        }

        JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    }
    single<TokenStorage> {
        DesktopTokenStorage()
    }
}