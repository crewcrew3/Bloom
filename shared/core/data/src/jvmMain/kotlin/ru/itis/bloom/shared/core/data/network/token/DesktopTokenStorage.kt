package ru.itis.bloom.shared.core.data.network.token

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.System.currentTimeMillis

class DesktopTokenStorage : TokenStorage {

    private val tokensFile = File(System.getProperty("user.home"), ".bloom/tokens.json")
    private val json = Json { ignoreUnknownKeys = true }

    private suspend fun readTokens(): TokensData = withContext(Dispatchers.IO) {
        if (!tokensFile.exists()) return@withContext TokensData()
        try {
            json.decodeFromString<TokensData>(tokensFile.readText())
        } catch (_: Exception) {
            TokensData()
        }
    }

    private suspend fun writeTokens(data: TokensData) = withContext(Dispatchers.IO) {
        tokensFile.parentFile?.mkdirs()
        tokensFile.writeText(json.encodeToString(data))
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        val expiresAtMillis = currentTimeMillis() + expiresIn.toLong() * 1000L
        writeTokens(TokensData(accessToken, refreshToken, expiresAtMillis))
        println("[BLOOM_TOKEN] Tokens saved to ${tokensFile.absolutePath}")
    }

    override suspend fun getAccessToken(): String? = readTokens().accessToken
    override suspend fun getRefreshToken(): String? = readTokens().refreshToken
    override suspend fun getExpiresAt(): Long? = readTokens().expiresAt

    override suspend fun isAccessTokenExpired(): Boolean {
        val expiresAt = getExpiresAt() ?: return true
        return System.currentTimeMillis() >= expiresAt
    }

    override suspend fun clearTokens() {
        writeTokens(TokensData())
        println("[BLOOM_TOKEN] Tokens cleared")
    }
    override suspend fun saveUserId(userId: String) {
        val current = readTokens()
        writeTokens(current.copy(userId = userId))
        println("[BLOOM_TOKEN] User ID saved: $userId")
    }

    override suspend fun getUserId(): String? = readTokens().userId
}