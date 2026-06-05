package ru.itis.bloom.shared.feature.profile.impl.navigation

import ru.itis.bloom.shared.core.navigation.api.ProfileNavigator
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileEffect
import ru.itis.bloom.shared.feature.profile.impl.mvi.details.ProfileDetailsEffect

internal class ProfileNavigationHandler(
    private val nav: ProfileNavigator
) {
    fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.NavigateToLogin -> {
                nav.toLoginScreen()
            }
            is ProfileEffect.NavigateToProfileDetails -> {
                nav.toProfileDetailsScreen()
            }
            is ProfileEffect.ShowMessage -> { /* handled by UI toast */
            }
        }
    }

    fun handleEffect(effect: ProfileDetailsEffect) {
        when (effect) {
            is ProfileDetailsEffect.NavigateToLogin -> nav.toLoginScreen()
            is ProfileDetailsEffect.NavigateBack -> nav.back()
            is ProfileDetailsEffect.ShowMessage -> { /* handled by UI toast */
            }
        }
    }
}