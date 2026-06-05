package ru.itis.bloom.shared.core.navigation.api

interface MakeupBagNavigator {
    fun toProductDetailScreen(productId: String)
    fun toCreateProductScreen()
    fun toEditProductScreen(productId: String)
    fun back()
    fun popToRoot()
}
