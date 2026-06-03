package ru.itis.bloom.shared.feature.makeupbag.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MakeupBagNavRoute : NavKey {
    @Serializable
    data object ProductList : MakeupBagNavRoute

    @Serializable
    data class ProductDetail(val productId: String) : MakeupBagNavRoute

    @Serializable
    data object CreateProduct : MakeupBagNavRoute

    @Serializable
    data class EditProduct(val productId: String) : MakeupBagNavRoute
}