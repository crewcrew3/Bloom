package ru.itis.bloom.shared.feature.auth.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthViewModel
import ru.itis.bloom.shared.feature.auth.impl.presentation.LoginScreen
import ru.itis.bloom.shared.feature.auth.impl.presentation.SignUpScreen

fun EntryProviderScope<NavKey>.authEntryBuilder() {
    entry<AuthNavRoute.Login> {
        val vm: AuthViewModel = koinViewModel()
        val navigationHandler: AuthNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleEffect)
        }

        LoginScreen(
            state = state,
            onIntent = vm::processIntent
        )
    }

    entry<AuthNavRoute.SignUp> {
        val vm: AuthViewModel = koinViewModel()
        val navigationHandler: AuthNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleEffect)
        }

        SignUpScreen(
            state = state,
            onIntent = vm::processIntent
        )
    }
}