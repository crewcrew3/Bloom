package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal sealed class ProductFormIntent {
    // Form field changes
    data class NameChanged(val name: String) : ProductFormIntent()
    data class BrandChanged(val brand: String) : ProductFormIntent()
    data class CategoryChanged(val category: ProductCategory) : ProductFormIntent()
    data class InciChanged(val inci: String) : ProductFormIntent()
    data class RatingChanged(val rating: Int) : ProductFormIntent()
    data class ReviewChanged(val review: String) : ProductFormIntent()
    data class OpenedDateChanged(val date: String) : ProductFormIntent()
    data class ShelfLifeChanged(val months: Int) : ProductFormIntent()
    data class FinishedChanged(val isFinished: Boolean) : ProductFormIntent()

    // Photo
    data class RequestPhotoSelection(val uri: String) : ProductFormIntent()
    data object RemovePhoto : ProductFormIntent()

    data class LoadProduct(val productId: String) : ProductFormIntent()
    data object Submit : ProductFormIntent()
    data object Archive : ProductFormIntent()
    data object Delete : ProductFormIntent()
    data object NavigateBack : ProductFormIntent()

    //data object ClearErrors : ProductFormIntent()
}