package ru.itis.bloom.shared.feature.profile.impl.mvi.details

import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

internal sealed class ProfileDetailsEffect {
    data object NavigateToLogin : ProfileDetailsEffect()
    data object NavigateBack : ProfileDetailsEffect()

    data class ShowMessage(val message: ProfileMessageRes) : ProfileDetailsEffect()
}