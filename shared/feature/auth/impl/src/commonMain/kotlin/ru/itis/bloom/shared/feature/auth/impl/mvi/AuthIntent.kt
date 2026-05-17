package ru.itis.bloom.shared.feature.auth.impl.mvi

internal sealed class AuthIntent {
    // Form field changes
    data class EmailChanged(val email: String) : AuthIntent()
    data class PasswordChanged(val password: String) : AuthIntent()
    data class NameChanged(val name: String) : AuthIntent()
    data class PasswordConfirmationChanged(val confirmation: String) : AuthIntent()

    // Auth actions
    data object LoginClicked : AuthIntent()
    data object RegisterClicked : AuthIntent()
    data class VerifyEmailClicked(val token: String) : AuthIntent()
    data object ForgotPasswordClicked : AuthIntent()
    data class ResetPasswordClicked(val email: String) : AuthIntent()
    data class ConfirmResetPasswordClicked(
        val token: String,
        val newPassword: String,
        val newPasswordConfirmation: String
    ) : AuthIntent()

    // UI actions
    data object ClearErrors : AuthIntent()
    data object NavigateToRegister : AuthIntent()
    data object NavigateToLogin : AuthIntent()
}
