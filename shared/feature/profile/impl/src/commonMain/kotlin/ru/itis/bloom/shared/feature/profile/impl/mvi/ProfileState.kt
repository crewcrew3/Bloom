package ru.itis.bloom.shared.feature.profile.impl.mvi

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile

internal data class ProfileState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false
)