package ru.itis.bloom.shared.feature.makeupbag.impl.navigation

import org.koin.core.component.KoinComponent
import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListEffect

internal class MakeupBagNavigationHandler(
    private val nav: BottomBarNavigator
) : KoinComponent {

    suspend fun handleListEffect(effect: ProductListEffect) {
        when (effect) {
            is ProductListEffect.NavigateToProductDetail -> { /* nav.toProductDetail(effect.productId) */ }
            is ProductListEffect.NavigateToCreateScreen -> { /* nav.toCreateProduct() */ }
            is ProductListEffect.ShowMessage -> { /* handled by UI snackbar */ }
        }
    }

    suspend fun handleFormEffect(effect: ProductFormEffect) {
        when (effect) {
            is ProductFormEffect.NavigateBack -> { /* nav.back() */ }
            is ProductFormEffect.NavigateBackAndRefresh -> { /* nav.backWithRefresh() */ }
            is ProductFormEffect.ShowMessage -> { /* handled by UI snackbar */ }
        }
    }

    suspend fun handleDetailEffect(effect: ProductDetailEffect) {
        when (effect) {
            is ProductDetailEffect.NavigateBack -> { /* nav.back() */ }
            is ProductDetailEffect.NavigateToEdit -> { /* nav.toEditProduct(effect.productId) */ }
            is ProductDetailEffect.ShowMessage -> { /* handled by UI snackbar */ }
        }
    }
}