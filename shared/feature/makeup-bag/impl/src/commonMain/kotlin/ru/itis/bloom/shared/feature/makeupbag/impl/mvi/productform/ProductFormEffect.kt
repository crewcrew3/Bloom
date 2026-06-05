package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

internal sealed class ProductFormEffect {
    data object NavigateBack : ProductFormEffect()
    data object NavigateBackToRoot : ProductFormEffect()
    //data object NavigateBackAndRefresh : ProductFormEffect()
    data class ShowMessage(val message: MakeupBagMessageRes) : ProductFormEffect()
}