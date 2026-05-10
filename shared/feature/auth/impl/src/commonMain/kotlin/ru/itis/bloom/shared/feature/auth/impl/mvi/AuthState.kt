package ru.itis.bloom.shared.feature.auth.impl.mvi

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile


data class AuthState(
    // Form fields
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val passwordConfirmation: String = "",

    // Validation errors
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
    val passwordConfirmationError: String? = null,
    val generalError: String? = null,

    // UI state
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isEmailVerified: Boolean = false,

    // Profile data
    val userProfile: UserProfile? = null
) {
    val isLoginFormValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank() &&
                emailError == null && passwordError == null

    val isRegisterFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() &&
                password.isNotBlank() && passwordConfirmation.isNotBlank() &&
                nameError == null && emailError == null &&
                passwordError == null && passwordConfirmationError == null
}

