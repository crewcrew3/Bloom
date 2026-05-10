package ru.itis.bloom.shared.feature.auth.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.compose.koinInject
import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.auth.impl.presentation.LoginScreen
import ru.itis.bloom.shared.feature.auth.impl.presentation.SignUpScreen

fun EntryProviderScope<NavKey>.authEntryBuilder() {
    entry<AuthNavRoute.Login> {
        //перенести навигаторы во вью модель и навигировать через нее, а не в компоуз экране
        val navigator: AuthNavigator = koinInject()
        LoginScreen(
            navigator = navigator
        )
    }
    entry<AuthNavRoute.SignUp> {
        val navigator: AuthNavigator = koinInject()
        SignUpScreen(
            navigator = navigator
        )
    }
}