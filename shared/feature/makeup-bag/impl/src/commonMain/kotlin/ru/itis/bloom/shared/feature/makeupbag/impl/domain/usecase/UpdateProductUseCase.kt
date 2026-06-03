package ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

internal class UpdateProductUseCase(
    private val repository: MakeupBagRepository
) {
    suspend operator fun invoke(
        id: String, request: CreateProductRequest,
        photoBytes: ByteArray?
    ): Result<Product> =
        repository.updateProduct(
            id = id,
            request = request,
            photoBytes = photoBytes
        )
}