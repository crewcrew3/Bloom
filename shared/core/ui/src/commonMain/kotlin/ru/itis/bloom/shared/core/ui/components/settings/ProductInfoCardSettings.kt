package ru.itis.bloom.shared.core.ui.components.settings

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus

data class ProductInfoCardSettings(
    val name: String,
    val brand: String?,
    val category: ProductCategory,
    val openedDate: String?,
    val shelfLifeAfterOpening: Int?,
    val expiryDate: String?,
    val status: ProductStatus,
    val inciComposition: String?,
    val personalRating: Int?,
    val personalReview: String?,
    val onArchiveClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onDeleteClick: () -> Unit
)