package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.MakeupBagNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute

class MakeupBagNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : MakeupBagNavigator {
    override fun toProductDetailScreen(productId: String) {
        backStackHolder.backStack?.add(MakeupBagNavRoute.ProductDetail(productId = productId))
    }

    override fun toCreateProductScreen() {
        backStackHolder.backStack?.add(MakeupBagNavRoute.CreateProduct)
    }

    override fun toEditProductScreen(productId: String) {
        backStackHolder.backStack?.add(MakeupBagNavRoute.EditProduct(productId = productId))
    }

    override fun back() {
        backStackHolder.backStack?.removeLastOrNull()
    }

    override fun popToRoot() {
        backStackHolder.backStack?.let { stack ->
            // Удаляем все экраны, пока не останется только корень (обычно это первый элемент)
            while (stack.size > 1) {
                stack.removeLastOrNull()
            }
        }
    }
}