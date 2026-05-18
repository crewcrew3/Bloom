package ru.itis.bloom.shared.feature.auth.api.error

import ru.itis.bloom.shared.core.data.error.BaseError

sealed interface AuthError : BaseError {
    data object InvalidCredentials : AuthError          // 401
    data object EmailNotVerified : AuthError            // 403
    data object EmailAlreadyExists : AuthError          // 409
    data object TokenInvalidOrExpired : AuthError       // 400
    data object RefreshTokenInvalid : AuthError         // 401
    data object WrongCurrentPassword : AuthError        // 401
    data object PasswordMismatch : AuthError            // 400
}