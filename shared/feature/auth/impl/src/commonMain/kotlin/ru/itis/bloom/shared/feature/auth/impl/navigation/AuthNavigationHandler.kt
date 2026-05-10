package ru.itis.bloom.shared.feature.auth.impl.navigation

import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthEffect
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthNavigationHandler : KoinComponent {
    private val nav: AuthNavigator by inject()

    suspend fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is AuthEffect.NavigateToMain -> {
                /* TODO: nav.toMainScreen() */
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

            is AuthEffect.TokensReceived -> {
                /* TODO: save tokens: effect.accessToken, effect.refreshToken */
            }

            is AuthEffect.TokensCleared -> {
                /* TODO: clear tokens */
            }

            else -> {
                // ShowMessage обрабатывается через Snackbar в BaseScreen
            }
        }
    }
}