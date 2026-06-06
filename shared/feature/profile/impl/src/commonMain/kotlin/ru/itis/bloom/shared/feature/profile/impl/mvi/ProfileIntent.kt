package ru.itis.bloom.shared.feature.profile.impl.mvi

internal sealed class ProfileIntent {
    data object LoadProfile : ProfileIntent()
    data object NavigateToProfileDetails : ProfileIntent()
    data object Logout : ProfileIntent()
}