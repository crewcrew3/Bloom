package ru.itis.bloom.shared.feature.auth.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("message")
    val message: String
)
