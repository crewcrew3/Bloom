package ru.itis.bloom

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.core.navigation.impl.navigationSavedStateConfig
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.auth.impl.navigation.authEntryBuilder

@Composable
fun App() {
    MaterialTheme {
        // Получаем все entry builders из графа Koin
        val entryBuilders: List<EntryProviderScope<NavKey>.() -> Unit> = listOf(
            EntryProviderScope<NavKey>::authEntryBuilder,
            // EntryProviderScope<NavKey>::profileEntryBuilder,
            // ... другие фичи
        )

        // Инициализируем BackStack с конфигурацией сериализации
        val backStack = rememberNavBackStack(
            navigationSavedStateConfig,
            AuthNavRoute.Login //наверное должна быть логика по проверке авторизации и если юзер в акке то перенапрвлять его на экран какой-нибудь фичи, а не на логин
        )

        val backStackHolder: BackStackHolder = koinInject()
        backStackHolder.setBackStack(backStack)

        // Основной UI
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                // Вызываем все builder'ы, чтобы зарегистрировать экраны
                entryBuilders.forEach { builder ->
                    this.builder()
                }
            }
        )
    }
}