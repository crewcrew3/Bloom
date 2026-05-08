package ru.itis.bloom.shared.feature.auth.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmResetPasswordRequest(
    @SerialName("token")
    val token: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirmation")
    val passwordConfirmation: String
)