package ru.itis.bloom.shared.feature.auth.impl.mvi

import ru.itis.bloom.shared.feature.auth.api.error.AuthError
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.request.LoginRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RegisterRequest

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>(extraBufferCapacity = 10)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun processIntent(intent: AuthIntent) {
        viewModelScope.launch {
            when (intent) {
                is AuthIntent.EmailChanged -> _state.update { it.copy(email = intent.email, emailError = null) }
                is AuthIntent.PasswordChanged -> _state.update { it.copy(password = intent.password, passwordError = null) }
                is AuthIntent.NameChanged -> _state.update { it.copy(name = intent.name, nameError = null) }
                is AuthIntent.PasswordConfirmationChanged -> _state.update { it.copy(passwordConfirmation = intent.confirmation, passwordConfirmationError = null) }

                is AuthIntent.LoginClicked -> handleLogin()
                is AuthIntent.RegisterClicked -> handleRegister()
                is AuthIntent.VerifyEmailClicked -> handleVerifyEmail(intent.token)
                is AuthIntent.ForgotPasswordClicked -> _effect.emit(AuthEffect.NavigateToForgotPasswordScreen)
                is AuthIntent.ResetPasswordClicked -> handleResetPassword(intent.email)
                is AuthIntent.ConfirmResetPasswordClicked -> handleConfirmResetPassword(intent.token, intent.newPassword, intent.newPasswordConfirmation)

                is AuthIntent.ClearErrors -> _state.update { it.copy(emailError = null, passwordError = null, nameError = null, passwordConfirmationError = null, generalError = null) }
                is AuthIntent.NavigateToRegister -> _effect.emit(AuthEffect.NavigateToRegisterScreen)
                is AuthIntent.NavigateToLogin -> _effect.emit(AuthEffect.NavigateToLoginScreen)
            }
        }
    }

    private suspend fun handleLogin() {
        val s = _state.value
        if (!validateLogin(s.email, s.password)) return

        _state.update { it.copy(isLoading = true, generalError = null) }

        when (val result = repository.login(LoginRequest(s.email, s.password))) {
            is Result.Success -> {
                _effect.emit(AuthEffect.Authenticated)
                _effect.emit(AuthEffect.NavigateToMain)
            }
            is Result.Error -> {
                val msg = mapErrorToMessage(result.error)
                _state.update { it.copy(isLoading = false, generalError = msg) }
                _effect.emit(AuthEffect.ShowErrorMessage(msg))
                if (result.error is AuthError.EmailNotVerified) _effect.emit(AuthEffect.NavigateToVerifyEmailScreen)
            }
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleRegister() {
        val s = _state.value
        if (!validateRegister(s.name, s.email, s.password, s.passwordConfirmation)) return

        _state.update { it.copy(isLoading = true, generalError = null) }

        when (val result = repository.register(RegisterRequest(s.name, s.email, s.password, s.passwordConfirmation))) {
            is Result.Success -> {
                _effect.emit(AuthEffect.VerificationEmailSent(s.email))
                _effect.emit(AuthEffect.ShowSuccessMessage("Проверьте email"))
                _effect.emit(AuthEffect.NavigateToLoginScreen)
            }
            is Result.Error -> {
                val msg = mapErrorToMessage(result.error)
                _state.update { it.copy(isLoading = false, generalError = msg) }
                _effect.emit(AuthEffect.ShowErrorMessage(msg))
            }
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleVerifyEmail(token: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.verifyEmail(token)) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false, isEmailVerified = true) }
                _effect.emit(AuthEffect.EmailVerified)
                _effect.emit(AuthEffect.ShowSuccessMessage("Аккаунт активирован"))
            }
            is Result.Error -> {
                val msg = mapErrorToMessage(result.error)
                _state.update { it.copy(isLoading = false, generalError = msg) }
                _effect.emit(AuthEffect.ShowErrorMessage(msg))
            }
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleResetPassword(email: String) {
        if (email.isBlank()) {
            _state.update { it.copy(emailError = "Email обязателен") }
            return
        }
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.resetPassword(ru.itis.bloom.shared.feature.auth.api.model.request.ResetPasswordRequest(email))) {
            is Result.Success -> _effect.emit(AuthEffect.ShowSuccessMessage("Ссылка отправлена"))
            is Result.Error -> _effect.emit(AuthEffect.ShowErrorMessage(mapErrorToMessage(result.error)))
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
        _state.update { it.copy(isLoading = false) }
    }

    private suspend fun handleConfirmResetPassword(token: String, pass: String, confirm: String) {
        if (pass != confirm) {
            _state.update { it.copy(passwordConfirmationError = "Пароли не совпадают") }
            return
        }
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.confirmResetPassword(ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest(token, pass, confirm))) {
            is Result.Success -> {
                _effect.emit(AuthEffect.ShowSuccessMessage("Пароль изменён"))
                _effect.emit(AuthEffect.NavigateToLoginScreen)
            }
            is Result.Error -> _effect.emit(AuthEffect.ShowErrorMessage(mapErrorToMessage(result.error)))
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
        _state.update { it.copy(isLoading = false) }
    }

    private fun validateLogin(email: String, pass: String): Boolean {
        var ok = true
        if (email.isBlank()) { _state.update { it.copy(emailError = "Email обязателен") }; ok = false }
        if (pass.length < 8) { _state.update { it.copy(passwordError = "Минимум 8 символов") }; ok = false }
        return ok
    }

    private fun validateRegister(name: String, email: String, pass: String, confirm: String): Boolean {
        var ok = true
        if (name.isBlank()) { _state.update { it.copy(nameError = "Имя обязательно") }; ok = false }
        if (email.isBlank()) { _state.update { it.copy(emailError = "Email обязателен") }; ok = false }
        if (pass.length < 8) { _state.update { it.copy(passwordError = "Минимум 8 символов") }; ok = false }
        if (pass != confirm) { _state.update { it.copy(passwordConfirmationError = "Пароли не совпадают") }; ok = false }
        return ok
    }

    private fun mapErrorToMessage(error: ru.itis.bloom.shared.core.data.error.BaseError): String = when (error) {
        is AuthError.InvalidCredentials -> "Неверный email или пароль"
        is AuthError.EmailNotVerified -> "Подтвердите email перед входом"
        is AuthError.EmailAlreadyExists -> "Этот email уже используется"
        is AuthError.TokenInvalidOrExpired -> "Токен недействителен или истёк"
        is AuthError.RefreshTokenInvalid -> "Необходима повторная авторизация"
        is AuthError.WrongCurrentPassword -> "Текущий пароль неверен"
        is AuthError.PasswordMismatch -> "Пароли не совпадают"
        else -> "Ошибка сервера. Попробуйте позже"
    }
}