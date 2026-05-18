package ru.itis.bloom.shared.feature.auth.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthNavRoute : NavKey {
    @Serializable
    data object Login : AuthNavRoute

    @Serializable
    data object SignUp : AuthNavRoute
}