package ru.itis.bloom.shared.feature.auth.impl.utils

import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.auth.api.error.AuthError
import bloom.shared.feature.auth.impl.generated.resources.*

internal sealed class AuthMessageRes {
    abstract fun toResourceId(): StringResource

    sealed class Error : AuthMessageRes() {
        data object InvalidCredentials : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_invalid_credentials
        }
        data object EmailNotVerified : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_email_not_verified
        }
        data object EmailAlreadyExists : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_email_already_exists
        }
        data object TokenInvalid : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_token_invalid
        }
        data object RefreshTokenInvalid : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_refresh_token_invalid
        }
        data object WrongPassword : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_wrong_password
        }
        data object PasswordMismatch : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_password_mismatch
        }
        data object Validation : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_validation
        }
        data object Network : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_network
        }
        data object Timeout : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_timeout
        }
        data object Unknown : Error() {
            override fun toResourceId(): StringResource = Res.string.auth_error_unknown
        }
    }

    sealed class Success : AuthMessageRes() {
        data object CheckEmail : Success() {
            override fun toResourceId(): StringResource = Res.string.auth_success_check_email
        }
        data object AccountActivated : Success() {
            override fun toResourceId(): StringResource = Res.string.auth_success_account_activated
        }
        data object LinkSent : Success() {
            override fun toResourceId(): StringResource = Res.string.auth_success_link_sent
        }
        data object PasswordChanged : Success() {
            override fun toResourceId(): StringResource = Res.string.auth_success_password_changed
        }
    }

    sealed class Validation : AuthMessageRes() {
        data object EmailRequired : Validation(){
            override fun toResourceId(): StringResource = Res.string.auth_validation_email_required
        }
        data object PasswordMinLength : Validation() {
            override fun toResourceId(): StringResource = Res.string.auth_validation_password_min_length
        }
        data object NameRequired : Validation() {
            override fun toResourceId(): StringResource = Res.string.auth_validation_name_required
        }
        data object PasswordsMismatch : Validation() {
            override fun toResourceId(): StringResource = Res.string.auth_validation_passwords_mismatch
        }
    }

    companion object {
        fun fromAuthError(error: AuthError): Error {
            return when (error) {
                is AuthError.InvalidCredentials -> Error.InvalidCredentials
                is AuthError.EmailNotVerified -> Error.EmailNotVerified
                is AuthError.EmailAlreadyExists -> Error.EmailAlreadyExists
                is AuthError.TokenInvalidOrExpired -> Error.TokenInvalid
                is AuthError.RefreshTokenInvalid -> Error.RefreshTokenInvalid
                is AuthError.WrongCurrentPassword -> Error.WrongPassword
                is AuthError.PasswordMismatch -> Error.PasswordMismatch
            }
        }
    }
}