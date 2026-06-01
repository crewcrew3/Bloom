package ru.itis.bloom.shared.feature.auth.impl.navigation

import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthEffect
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AuthNavigationHandler(
    private val nav: AuthNavigator
) : KoinComponent {

    suspend fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is AuthEffect.NavigateToMain -> {
                nav.toMainScreen()
            }

            is AuthEffect.NavigateToRegisterScreen -> {
                nav.toSignUpScreen()
            }

            is AuthEffect.NavigateToLoginScreen -> {
                nav.toLoginScreen()
            }

            is AuthEffect.NavigateToVerifyEmailScreen -> {
                /* TODO: nav.toVerifyEmailScreen() */
            }

            is AuthEffect.NavigateToForgotPasswordScreen -> {
                /* TODO: nav.toForgotPasswordScreen() */
            }

            else -> {
                // ShowMessage обрабатывается через Snackbar в BaseScreen
            }
        }
    }
}