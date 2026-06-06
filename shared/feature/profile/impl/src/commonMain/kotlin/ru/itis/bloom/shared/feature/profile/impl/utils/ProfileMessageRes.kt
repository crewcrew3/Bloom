package ru.itis.bloom.shared.feature.profile.impl.utils

import org.jetbrains.compose.resources.StringResource
import bloom.shared.feature.profile.impl.generated.resources.*
import ru.itis.bloom.shared.feature.profile.api.error.ProfileError

internal sealed class ProfileMessageRes {
    abstract fun toResourceId(): StringResource

    sealed class Error : ProfileMessageRes() {
        data object ProfileNotFound : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_not_found
        }

        data object EmailAlreadyExists : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_email_already_exists
        }

        data object PasswordMismatch : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_password_mismatch
        }

        data object WrongCurrentPassword : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_wrong_password
        }

        data object ImageTooLarge : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_image_too_large
        }

        data object UnsupportedImageFormat : Error() {
            override fun toResourceId(): StringResource =
                Res.string.profile_error_unsupported_format
        }

        data object EmptyName : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_empty_name
        }

        data object EmptyEmail : Error() {
            override fun toResourceId(): StringResource = Res.string.profile_error_empty_email
        }


        data object PasswordTooShort : Error() {
            override fun toResourceId(): StringResource =
                Res.string.profile_error_password_too_short
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

        data object ProfileUpdated : Success() {
            override fun toResourceId(): StringResource = Res.string.profile_success_profile_updated
        }

        data object PasswordChanged : Success() {
            override fun toResourceId(): StringResource = Res.string.profile_success_password_changed
        }
    }

    companion object {
        fun fromProfileError(error: ProfileError): Error {
            return when (error) {
                is ProfileError.ProfileNotFound -> Error.ProfileNotFound
                ProfileError.EmailAlreadyExists -> Error.EmailAlreadyExists
                ProfileError.PasswordMismatch -> Error.PasswordMismatch
                ProfileError.WrongCurrentPassword -> Error.WrongCurrentPassword
            }
        }
    }
}