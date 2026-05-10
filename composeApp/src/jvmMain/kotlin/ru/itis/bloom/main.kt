package ru.itis.bloom

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import ru.itis.bloom.shared.core.data.di.networkModule
import ru.itis.bloom.shared.core.navigation.impl.di.navigationModule
import ru.itis.bloom.shared.feature.auth.impl.di.authModule
import ru.itis.bloom.shared.feature.auth.impl.di.authNavModule

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
                navigationModule,
                authNavModule,
                authModule,
                networkModule
                // ... другие модули
            )
        }
    }
}