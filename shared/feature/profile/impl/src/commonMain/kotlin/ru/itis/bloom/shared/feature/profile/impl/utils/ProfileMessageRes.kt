package ru.itis.bloom.shared.feature.profile.impl.utils

import org.jetbrains.compose.resources.StringResource
import bloom.shared.feature.profile.impl.generated.resources.*

internal sealed class ProfileMessageRes {
    abstract fun toResourceId(): StringResource

    sealed class Error : ProfileMessageRes() {
        data object ProfileNotFound : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_not_found
        }
        data object Unauthorized : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_unauthorized
        }
        data object Network : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_network
        }
        data object Timeout : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_timeout
        }
        data object ServerError : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_server
        }
        data object Unknown : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_unknown
        }
    }

    sealed class Success : ProfileMessageRes() {
        data object LoggedOut : Success() {
            override fun toResourceId(): StringResource = Res.string.profile_success_logged_out
        }
    }

    companion object {
        fun fromProfileError(error: ru.itis.bloom.shared.feature.profile.api.error.ProfileError): Error {
            return when (error) {
                is ru.itis.bloom.shared.feature.profile.api.error.ProfileError.ProfileNotFound -> Error.ProfileNotFound
            }
        }
    }
}