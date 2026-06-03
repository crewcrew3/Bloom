package ru.itis.bloom.shared.core.data.network.token

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.System.currentTimeMillis
import androidx.core.content.edit

class AndroidTokenStorage(context: Context) : TokenStorage {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "bloom_tokens", Context.MODE_PRIVATE
    )

    private companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        withContext(Dispatchers.IO) {
            val expiresAtMillis = currentTimeMillis() + expiresIn.toLong() * 1000L
            prefs.edit {
                putString(KEY_ACCESS, accessToken)
                putString(KEY_REFRESH, refreshToken)
                putLong(KEY_EXPIRES_AT, expiresAtMillis)
            }
        }
        println("[BLOOM_TOKEN] Tokens saved, expires at: ${currentTimeMillis() + expiresIn * 1000}")
    }

    override suspend fun getAccessToken(): String? =
        withContext(Dispatchers.IO) { prefs.getString(KEY_ACCESS, null) }

    override suspend fun getRefreshToken(): String? =
        withContext(Dispatchers.IO) { prefs.getString(KEY_REFRESH, null) }

    override suspend fun getExpiresAt(): Long? =
        withContext(Dispatchers.IO) {
            val expires = prefs.getLong(KEY_EXPIRES_AT, -1L)
            if (expires == -1L) null else expires
        }

    override suspend fun isAccessTokenExpired(): Boolean {
        val expiresAt = getExpiresAt() ?: return true
        return currentTimeMillis() >= expiresAt
    }

    override suspend fun clearTokens() {
        withContext(Dispatchers.IO) {
            prefs.edit { clear() }
        }
        println("[BLOOM_TOKEN] Tokens cleared")
    }
}