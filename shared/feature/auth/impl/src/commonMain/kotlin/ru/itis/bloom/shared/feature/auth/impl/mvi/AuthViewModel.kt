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
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.core.ui.analytics.AnalyticsHelper
import ru.itis.bloom.shared.core.ui.analytics.ScreenName
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.request.LoginRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RegisterRequest
import ru.itis.bloom.shared.feature.auth.impl.utils.AuthMessageRes

private const val TAG = "BLOOM_AUTH_VM"

internal class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    init {
        AnalyticsHelper.logScreenOpen(ScreenName.AUTH)
    }

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>(extraBufferCapacity = 10)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun processIntent(intent: AuthIntent) {
        viewModelScope.launch {
            when (intent) {
                is AuthIntent.EmailChanged -> _state.update {
                    it.copy(
                        email = intent.email,
                        emailError = null
                    )
                }

                is AuthIntent.PasswordChanged -> _state.update {
                    it.copy(
                        password = intent.password,
                        passwordError = null
                    )
                }

                is AuthIntent.NameChanged -> _state.update {
                    it.copy(
                        name = intent.name,
                        nameError = null
                    )
                }

                is AuthIntent.PasswordConfirmationChanged -> _state.update {
                    it.copy(
                        passwordConfirmation = intent.confirmation,
                        passwordConfirmationError = null
                    )
                }

                is AuthIntent.LoginClicked -> handleLogin()
                is AuthIntent.RegisterClicked -> handleRegister()
                is AuthIntent.VerifyEmailClicked -> handleVerifyEmail(intent.token)
                is AuthIntent.ForgotPasswordClicked -> _effect.emit(AuthEffect.NavigateToForgotPasswordScreen)
                is AuthIntent.ResetPasswordClicked -> handleResetPassword(intent.email)
                is AuthIntent.ConfirmResetPasswordClicked -> handleConfirmResetPassword(
                    intent.token,
                    intent.newPassword,
                    intent.newPasswordConfirmation
                )

                is AuthIntent.ClearErrors -> _state.update {
                    it.copy(
                        emailError = null,
                        passwordError = null,
                        nameError = null,
                        passwordConfirmationError = null,
                        generalError = null
                    )
                }

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
                val messageRes = mapErrorToMessageRes(result.error)
                _state.update {
                    it.copy(
                        isLoading = false,
                        generalError = if (messageRes is AuthMessageRes.Error) messageRes.toResourceId() else null
                    )
                }
                _effect.emit(AuthEffect.ShowMessage(messageRes))
                if (result.error is AuthError.EmailNotVerified) _effect.emit(AuthEffect.NavigateToVerifyEmailScreen)
            }

            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleRegister() {
        println("[$TAG] handleRegister() → start")
        val s = _state.value
        if (!validateRegister(s.name, s.email, s.password, s.passwordConfirmation)) {
            println("[$TAG] handleRegister() → validation failed")
            return
        }

        _state.update { it.copy(isLoading = true, generalError = null) }
        println("[$TAG] → repository.register()")

        when (val result = repository.register(RegisterRequest(s.name, s.email, s.password, s.passwordConfirmation))) {
            is Result.Success -> {
                println("[$TAG] ← register() Success")
                _effect.emit(AuthEffect.VerificationEmailSent(s.email))
                _effect.emit(AuthEffect.ShowMessage(AuthMessageRes.Success.CheckEmail))
                _effect.emit(AuthEffect.NavigateToLoginScreen)
            }
            is Result.Error -> {
                println("[$TAG] ← register() Error: ${result.error::class.simpleName}")
                val messageRes = mapErrorToMessageRes(result.error)
                _state.update { it.copy(isLoading = false, generalError =  if (messageRes is AuthMessageRes.Error) messageRes.toResourceId() else null) }
                _effect.emit(AuthEffect.ShowMessage(messageRes))
            }
            is Result.Loading -> {
                println("[$TAG] ← register() Loading")
                _state.update { it.copy(isLoading = true) }
            }
        }
        println("[$TAG] handleRegister() → end")
    }

    private suspend fun handleVerifyEmail(token: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.verifyEmail(token)) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false, isEmailVerified = true) }
                _effect.emit(AuthEffect.EmailVerified)
                _effect.emit(AuthEffect.ShowMessage(AuthMessageRes.Success.AccountActivated))
            }

            is Result.Error -> {
                val messageRes = mapErrorToMessageRes(result.error)
                _state.update {
                    it.copy(
                        isLoading = false,
                        generalError = if (messageRes is AuthMessageRes.Error) messageRes.toResourceId() else null
                    )
                }
                _effect.emit(AuthEffect.ShowMessage(messageRes))
            }

            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleResetPassword(email: String) {
        if (email.isBlank()) {
            _state.update { it.copy(emailError = AuthMessageRes.Validation.EmailRequired.toResourceId()) }
            return
        }
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.resetPassword(
            ru.itis.bloom.shared.feature.auth.api.model.request.ResetPasswordRequest(email)
        )) {
            is Result.Success -> _effect.emit(AuthEffect.ShowMessage(AuthMessageRes.Success.LinkSent))
            is Result.Error -> _effect.emit(AuthEffect.ShowMessage(mapErrorToMessageRes(result.error)))
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
        _state.update { it.copy(isLoading = false) }
    }

    private suspend fun handleConfirmResetPassword(token: String, pass: String, confirm: String) {
        if (pass != confirm) {
            _state.update { it.copy(passwordConfirmationError = AuthMessageRes.Validation.PasswordsMismatch.toResourceId()) }
            return
        }
        _state.update { it.copy(isLoading = true) }
        when (val result = repository.confirmResetPassword(
            ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest(
                token,
                pass,
                confirm
            )
        )) {
            is Result.Success -> {
                _effect.emit(AuthEffect.ShowMessage(AuthMessageRes.Success.PasswordChanged))
                _effect.emit(AuthEffect.NavigateToLoginScreen)
            }

            is Result.Error -> _effect.emit(AuthEffect.ShowMessage(mapErrorToMessageRes(result.error)))
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
        _state.update { it.copy(isLoading = false) }
    }

    private fun validateLogin(email: String, pass: String): Boolean {
        var ok = true
        if (email.isBlank()) {
            _state.update { it.copy(emailError = AuthMessageRes.Validation.EmailRequired.toResourceId()) }; ok =
                false
        }
        if (pass.length < 8) {
            _state.update { it.copy(passwordError = AuthMessageRes.Validation.PasswordMinLength.toResourceId()) }; ok =
                false
        }
        return ok
    }

    private fun validateRegister(
        name: String,
        email: String,
        pass: String,
        confirm: String
    ): Boolean {
        var ok = true
        if (name.isBlank()) {
            _state.update { it.copy(nameError = AuthMessageRes.Validation.NameRequired.toResourceId()) }; ok =
                false
        }
        if (email.isBlank()) {
            _state.update { it.copy(emailError = AuthMessageRes.Validation.EmailRequired.toResourceId()) }; ok =
                false
        }
        if (pass.length < 8) {
            _state.update { it.copy(passwordError = AuthMessageRes.Validation.PasswordMinLength.toResourceId()) }; ok =
                false
        }
        if (pass != confirm) {
            _state.update { it.copy(passwordConfirmationError = AuthMessageRes.Validation.PasswordsMismatch.toResourceId()) }; ok =
                false
        }
        return ok
    }

    private fun mapErrorToMessageRes(error: BaseError): AuthMessageRes {
        println("[$TAG] mapErrorToMessageRes() → ${error::class.simpleName}")
        return when (error) {
            is AuthError -> {
                println("[$TAG] → AuthError: ${error::class.simpleName}")
                AuthMessageRes.fromAuthError(error)
            }
            is CommonError -> when (error) {
                is CommonError.ValidationError -> { println("[$TAG] → CommonError.ValidationError"); AuthMessageRes.Error.Validation }
                is CommonError.NetworkUnavailable -> { println("[$TAG] → CommonError.NetworkUnavailable"); AuthMessageRes.Error.Network }
                is CommonError.Timeout -> { println("[$TAG] → CommonError.Timeout"); AuthMessageRes.Error.Timeout }
                else -> { println("[$TAG] → CommonError.Unknown"); AuthMessageRes.Error.Unknown }
            }
            else -> { println("[$TAG] → Fallback to Unknown"); AuthMessageRes.Error.Unknown }
        }
    }
}