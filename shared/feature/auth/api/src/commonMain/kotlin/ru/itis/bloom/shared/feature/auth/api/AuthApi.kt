package ru.itis.bloom.shared.feature.auth.api

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.LoginRequest
import ru.itis.bloom.shared.core.data.model.RefreshTokenRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RegisterRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.ResetPasswordRequest
import ru.itis.bloom.shared.core.data.model.AuthTokensResponse
import ru.itis.bloom.shared.feature.auth.api.model.response.MessageResponse

interface AuthApi {
    suspend fun register(request: RegisterRequest): BloomResult<MessageResponse>
    suspend fun verifyEmail(token: String): BloomResult<MessageResponse>
    suspend fun login(request: LoginRequest): BloomResult<Unit>
    suspend fun refreshToken(request: RefreshTokenRequest): BloomResult<AuthTokensResponse>
    suspend fun resetPassword(request: ResetPasswordRequest): BloomResult<MessageResponse>
    suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): BloomResult<MessageResponse>
}