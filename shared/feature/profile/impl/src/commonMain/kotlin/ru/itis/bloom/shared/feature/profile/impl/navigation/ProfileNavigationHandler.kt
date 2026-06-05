package ru.itis.bloom.shared.feature.profile.impl.navigation

import ru.itis.bloom.shared.core.navigation.api.ProfileNavigator
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileEffect

internal class ProfileNavigationHandler(
    private val nav: ProfileNavigator
) {
    fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.NavigateToLogin -> { nav.toLoginScreen() }
            is ProfileEffect.ShowMessage -> { /* handled by UI toast */ }
        }
    }
}