package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail

import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product

internal data class ProductDetailState(
    val product: Product? = null,
    val isLoading: Boolean = false,
)