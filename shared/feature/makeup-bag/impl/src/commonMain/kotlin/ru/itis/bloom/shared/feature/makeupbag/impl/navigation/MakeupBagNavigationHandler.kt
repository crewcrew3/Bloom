package ru.itis.bloom.shared.feature.makeupbag.impl.navigation

import ru.itis.bloom.shared.core.navigation.api.MakeupBagNavigator
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListEffect

internal class MakeupBagNavigationHandler(
    private val nav: MakeupBagNavigator,
) {
    fun handleListEffect(effect: ProductListEffect) {
        when (effect) {
            is ProductListEffect.NavigateToProductDetail -> { nav.toProductDetailScreen(effect.productId) }
            is ProductListEffect.NavigateToCreateScreen -> { nav.toCreateProductScreen() }
            is ProductListEffect.ShowMessage -> { /* обрабатывается в UI Toast-ом */ }
        }
    }

    fun handleFormEffect(effect: ProductFormEffect) {
        when (effect) {
            is ProductFormEffect.NavigateBack -> { nav.back() }
            //is ProductFormEffect.NavigateBackAndRefresh -> { /* nav.backWithRefresh() */ }
            is ProductFormEffect.ShowMessage -> { /* обрабатывается в UI Toast-ом */ }
        }
    }

    fun handleDetailEffect(effect: ProductDetailEffect) {
        when (effect) {
            is ProductDetailEffect.NavigateBack -> { nav.back() }
            is ProductDetailEffect.NavigateToEdit -> { nav.toEditProductScreen(effect.productId) }
            is ProductDetailEffect.ShowMessage -> { /* обрабатывается в UI Toast-ом */ }
        }
    }
}