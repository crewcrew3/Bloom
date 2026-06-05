package ru.itis.bloom.shared.feature.profile.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavRoute : NavKey {
    @Serializable
    data object Profile : ProfileNavRoute
    @Serializable
    data object ProfileDetails : ProfileNavRoute
}