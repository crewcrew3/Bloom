package ru.itis.bloom.shared.feature.auth.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("error")
    val error: String,
    @SerialName("message")
    val message: String,
    @SerialName("details")
    val details: List<FieldError>? = null
) {
    @Serializable
    data class FieldError(
        @SerialName("field")
        val field: String,
        @SerialName("message")
        val message: String
    )
}
