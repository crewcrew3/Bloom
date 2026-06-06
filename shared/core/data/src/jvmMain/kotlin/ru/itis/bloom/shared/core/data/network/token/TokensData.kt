package ru.itis.bloom.shared.core.data.network.token

import kotlinx.serialization.Serializable

@Serializable
data class TokensData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: Long? = null,
    val userId: String? = null
)