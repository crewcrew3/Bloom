package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail

internal sealed class ProductDetailIntent {
    data class LoadProduct(val productId: String) : ProductDetailIntent()
    data object NavigateToEdit : ProductDetailIntent()
    data object Archive : ProductDetailIntent()
    data object Delete : ProductDetailIntent()
    data object NavigateBack : ProductDetailIntent()
    data object ClearErrors : ProductDetailIntent()
}