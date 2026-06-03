package ru.itis.bloom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.core.navigation.impl.navigationSavedStateConfig
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.auth.impl.navigation.authEntryBuilder
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.makeupbag.impl.navigation.makeupBagEntryBuilder
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation.diaryEntryBuilder

@Composable
fun App() {
    BloomTheme {
        // Получаем все entry builders из графа Koin
        val entryBuilders: List<EntryProviderScope<NavKey>.() -> Unit> = listOf(
            EntryProviderScope<NavKey>::authEntryBuilder,
            EntryProviderScope<NavKey>::makeupBagEntryBuilder,
            EntryProviderScope<NavKey>::diaryEntryBuilder,
            // ... другие фичи
        )
        var isAuthChecked by remember { mutableStateOf(false) }
        var initialRoute by remember { mutableStateOf<NavKey>(AuthNavRoute.Login) }

        // Получаем TokenStorage для проверки
        val tokenStorage: TokenStorage = koinInject()
        LaunchedEffect(Unit) {
            val accessToken = tokenStorage.getAccessToken()
            val isExpired = tokenStorage.isAccessTokenExpired()

            initialRoute = if (accessToken != null && !isExpired) {
                println("[BLOOM_APP] User authenticated, navigating to MakeupBag")
                MakeupBagNavRoute.ProductList  // ← Главный экран косметички
            } else {
                println("[BLOOM_APP] User not authenticated, showing Login")
                AuthNavRoute.Login
            }
            isAuthChecked = true
        }

        if (!isAuthChecked) {
            // Опционально: простой сплэш-экран
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@BloomTheme
        }

        // Инициализируем BackStack с конфигурацией сериализации
        val backStack = rememberNavBackStack(
            navigationSavedStateConfig,
            initialRoute //наверное должна быть логика по проверке авторизации и если юзер в акке то перенапрвлять его на экран какой-нибудь фичи, а не на логин
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