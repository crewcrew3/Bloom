package ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository

internal class DeleteProductUseCase(
    private val repository: MakeupBagRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteProduct(id)
}