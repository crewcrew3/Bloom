package ru.itis.bloom.shared.feature.profile.impl.mvi.details

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

internal data class ProfileDetailsState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val activeDialog: EditDialogType? = null,
    val dialogError: ProfileMessageRes? = null
)