package ru.itis.bloom

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import ru.itis.bloom.shared.core.data.di.commonDatabaseModule
import ru.itis.bloom.shared.core.data.di.networkModule
import ru.itis.bloom.shared.core.data.di.platformModule
import ru.itis.bloom.shared.core.data.di.qualifierModule
import ru.itis.bloom.shared.core.domain.di.coreDomainModule
import ru.itis.bloom.shared.core.navigation.impl.di.navigationModule
import ru.itis.bloom.shared.feature.auth.impl.di.authModule
import ru.itis.bloom.shared.feature.auth.impl.di.authNavModule
import ru.itis.bloom.shared.feature.makeupbag.impl.di.makeupBagModule
import ru.itis.bloom.shared.feature.makeupbag.impl.di.makeupBagNavModule
import ru.itis.bloom.shared.feature.skindiary.impl.di.diaryModule
import ru.itis.bloom.shared.feature.skindiary.impl.di.diaryNavModule

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Bloom",
        ) {
            App()
        }
    }
}

// Инициализация Koin (при старте приложения)
private fun initKoin() {
    if (GlobalContext.getOrNull() == null) {
        startKoin {
            modules(
                qualifierModule,

                platformModule,
                networkModule,
                commonDatabaseModule,
                coreDomainModule,

                navigationModule,
                authNavModule,
                authModule,
                makeupBagNavModule,
                makeupBagModule,
                diaryModule(),
                diaryNavModule
                // ... другие модули
            )
        }
    }
}