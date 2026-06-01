package ru.itis.bloom.shared.core.data.network.token

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.model.AuthTokensResponse
import ru.itis.bloom.shared.core.data.model.RefreshTokenRequest

interface TokenRefresher {
    suspend fun refreshTokens(request: RefreshTokenRequest): Result<AuthTokensResponse>
}