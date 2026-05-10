package ru.itis.bloom.shared.feature.auth.api

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.LoginRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RefreshTokenRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.RegisterRequest
import ru.itis.bloom.shared.feature.auth.api.model.request.ResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.response.AuthTokensResponse
import ru.itis.bloom.shared.feature.auth.api.model.response.MessageResponse
import ru.itis.bloom.shared.core.data.Result as BloomResult

interface AuthRepository {
    suspend fun register(request: RegisterRequest): BloomResult<MessageResponse>
    suspend fun verifyEmail(token: String): BloomResult<MessageResponse>
    suspend fun login(request: LoginRequest): BloomResult<AuthTokensResponse>
    suspend fun refreshToken(request: RefreshTokenRequest): BloomResult<AuthTokensResponse>
    suspend fun logout(request: RefreshTokenRequest): BloomResult<MessageResponse>
    suspend fun resetPassword(request: ResetPasswordRequest): BloomResult<MessageResponse>
    suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): BloomResult<MessageResponse>
    suspend fun getProfile(): BloomResult<UserProfile>
}