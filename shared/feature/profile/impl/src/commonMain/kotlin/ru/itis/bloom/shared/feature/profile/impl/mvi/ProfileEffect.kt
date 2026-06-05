package ru.itis.bloom.shared.feature.profile.impl.mvi

import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

internal sealed class ProfileEffect {
    data object NavigateToLogin : ProfileEffect()
    data class ShowMessage(val message: ProfileMessageRes) : ProfileEffect()
}