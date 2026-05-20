package ru.itis.bloom.shared.feature.makeupbag.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

@Serializable
data class CreateProductRequest(
    @SerialName("name") val name: String,
    @SerialName("brand") val brand: String? = null,
    @SerialName("category") val category: ProductCategory,
    @SerialName("inci_composition") val inciComposition: String? = null,
    @SerialName("personal_rating") val personalRating: Int? = null,
    @SerialName("personal_review") val personalReview: String? = null,
    @SerialName("opened_date") val openedDate: String? = null,
    @SerialName("shelf_life_after_opening") val shelfLifeAfterOpening: Int? = null,
    //@SerialName("photo") val photo: какой-то_тип_данных? = null //тут надо изменить
)