package ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

internal class CreateProductUseCase(
    private val repository: MakeupBagRepository
) {
    suspend operator fun invoke(request: CreateProductRequest): Result<Product> =
        repository.createProduct(request)
}