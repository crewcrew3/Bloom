package ru.itis.bloom.shared.feature.auth.impl.mvi

sealed class AuthEffect {
    // Navigation
    data object NavigateToMain : AuthEffect()
    data object NavigateToRegisterScreen : AuthEffect()
    data object NavigateToLoginScreen : AuthEffect()
    data object NavigateToVerifyEmailScreen : AuthEffect()
    data object NavigateToForgotPasswordScreen : AuthEffect()

    // Messages
    data class ShowSuccessMessage(val message: String) : AuthEffect()
    data class ShowErrorMessage(val message: String) : AuthEffect()

    // Token management
    data object Authenticated : AuthEffect()

    // Email verification
    data class VerificationEmailSent(val email: String) : AuthEffect()
    data object EmailVerified : AuthEffect()
}