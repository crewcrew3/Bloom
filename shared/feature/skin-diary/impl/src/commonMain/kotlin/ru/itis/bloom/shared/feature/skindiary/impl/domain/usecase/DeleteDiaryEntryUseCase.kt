package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository

internal class DeleteDiaryEntryUseCase(
    private val repository: SkinDiaryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteEntry(id)
    }
}