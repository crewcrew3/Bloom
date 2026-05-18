package ru.itis.bloom.shared.feature.auth.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    @SerialName("email")
    val email: String
)