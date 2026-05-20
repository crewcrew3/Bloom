package ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product

internal class GetProductByIdUseCase(
    private val repository: MakeupBagRepository
) {
    suspend operator fun invoke(id: String): Result<Product> = repository.getProductById(id)
}