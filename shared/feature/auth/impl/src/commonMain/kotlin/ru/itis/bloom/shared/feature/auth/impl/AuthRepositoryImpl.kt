package ru.itis.bloom.shared.feature.auth.impl

import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.auth.api.model.request.*
import ru.itis.bloom.shared.feature.auth.api.model.response.*

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun register(request: RegisterRequest): BloomResult<MessageResponse> {
        return authApi.register(request)
    }

    override suspend fun verifyEmail(token: String): BloomResult<MessageResponse> {
        return authApi.verifyEmail(token)
    }

    override suspend fun login(request: LoginRequest): BloomResult<AuthTokensResponse> {
        return authApi.login(request)
    }

    override suspend fun refreshToken(request: RefreshTokenRequest): BloomResult<AuthTokensResponse> {
        return authApi.refreshToken(request)
    }

    override suspend fun logout(request: RefreshTokenRequest): BloomResult<MessageResponse> {
        return authApi.logout(request)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): BloomResult<MessageResponse> {
        return authApi.resetPassword(request)
    }

    override suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): BloomResult<MessageResponse> {
        return authApi.confirmResetPassword(request)
    }

    override suspend fun getProfile(): BloomResult<UserProfile> {
        // TODO: Implement when ProfileApi is ready
        return BloomResult.Error(CommonError.Unknown)
    }
}