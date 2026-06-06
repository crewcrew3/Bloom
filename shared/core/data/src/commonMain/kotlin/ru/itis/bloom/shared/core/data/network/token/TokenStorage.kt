package ru.itis.bloom.shared.core.data.network.token

interface TokenStorage {
    suspend fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getExpiresAt(): Long? // timestamp в миллисекундах
    suspend fun isAccessTokenExpired(): Boolean
    suspend fun clearTokens()

    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String?
}