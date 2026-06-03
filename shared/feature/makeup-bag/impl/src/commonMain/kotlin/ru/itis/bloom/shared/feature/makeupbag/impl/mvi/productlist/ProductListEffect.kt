package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

internal sealed class ProductListEffect {
    data class NavigateToProductDetail(val productId: String) : ProductListEffect()
    data object NavigateToCreateScreen : ProductListEffect()
    data class ShowMessage(val message: MakeupBagMessageRes) : ProductListEffect()
}