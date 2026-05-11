package ru.itis.bloom.shared.feature.auth.impl.data

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.LoginRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RefreshTokenRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RegisterRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.ResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.response.AuthTokensResponse
import ru.itis.bloom.shared.feature.auth.api.model.response.MessageResponse

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): Result<MessageResponse> {
        return authApi.register(request)
    }

    override suspend fun verifyEmail(token: String): Result<MessageResponse> {
        return authApi.verifyEmail(token)
    }

    override suspend fun login(request: LoginRequest): Result<Unit> {
        return authApi.login(request)
    }

    override suspend fun refreshToken(request: RefreshTokenRequest): Result<AuthTokensResponse> {
        return authApi.refreshToken(request)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<MessageResponse> {
        return authApi.resetPassword(request)
    }

    override suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): Result<MessageResponse> {
        return authApi.confirmResetPassword(request)
    }
}