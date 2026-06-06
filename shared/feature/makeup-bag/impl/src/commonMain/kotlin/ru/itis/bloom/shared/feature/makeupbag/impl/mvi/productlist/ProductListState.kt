package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal data class ProductListState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val filterCategory: ProductCategory? = null
)