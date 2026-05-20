package ru.itis.bloom.shared.feature.makeupbag.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProductStatus {
    @SerialName("active") Active,
    @SerialName("archived") Archived
}