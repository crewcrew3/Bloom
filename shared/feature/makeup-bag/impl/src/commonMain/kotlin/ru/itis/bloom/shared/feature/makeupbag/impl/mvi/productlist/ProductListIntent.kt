package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal sealed class ProductListIntent {
    data object LoadProducts : ProductListIntent()
    data object Refresh : ProductListIntent()
    data class FilterByCategory(val category: ProductCategory?) : ProductListIntent()
    data class SelectProduct(val productId: String) : ProductListIntent()
    data object NavigateToCreate : ProductListIntent()
}