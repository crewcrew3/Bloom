package ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus

internal class GetProductsUseCase(
    private val repository: MakeupBagRepository
) {
    suspend operator fun invoke(
        category: ProductCategory? = null,
        status: ProductStatus = ProductStatus.Active
    ): Result<List<Product>> = repository.getProducts(category, status)
}