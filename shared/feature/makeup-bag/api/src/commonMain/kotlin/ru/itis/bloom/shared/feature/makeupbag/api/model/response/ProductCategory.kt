package ru.itis.bloom.shared.feature.makeupbag.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProductCategory {
    @SerialName("cleanser") Cleanser,
    @SerialName("toner") Toner,
    @SerialName("serum") Serum,
    @SerialName("moisturizer") Moisturizer,
    @SerialName("sunscreen") Sunscreen,
    @SerialName("mask") Mask,
    @SerialName("eye_cream") EyeCream,
    @SerialName("exfoliant") Exfoliant,
    @SerialName("oil") Oil,
    @SerialName("other") Other
}