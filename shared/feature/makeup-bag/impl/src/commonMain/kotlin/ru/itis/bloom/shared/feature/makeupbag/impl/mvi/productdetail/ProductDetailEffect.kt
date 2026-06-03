package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail

import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

internal sealed class ProductDetailEffect {
    data object NavigateBack : ProductDetailEffect()
    data class NavigateToEdit(val productId: String) : ProductDetailEffect()
    data class ShowMessage(val message: MakeupBagMessageRes) : ProductDetailEffect()
}