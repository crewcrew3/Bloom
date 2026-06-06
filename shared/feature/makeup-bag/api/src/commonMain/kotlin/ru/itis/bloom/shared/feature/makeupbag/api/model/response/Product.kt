package ru.itis.bloom.shared.feature.makeupbag.api.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("brand") val brand: String? = null,
    @SerialName("category") val category: ProductCategory,
    @SerialName("inci_composition") val inciComposition: String? = null,
    @SerialName("personal_rating") val personalRating: Int? = null,
    @SerialName("personal_review") val personalReview: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("opened_date") val openedDate: String? = null,
    @SerialName("shelf_life_after_opening") val shelfLifeAfterOpening: Int? = null,
    @SerialName("expiry_date") val expiryDate: String? = null,
    @SerialName("status") val status: ProductStatus,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)