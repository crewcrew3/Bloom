package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal data class ProductListState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val generalError: StringResource? = null,
    val filterCategory: ProductCategory? = null
)