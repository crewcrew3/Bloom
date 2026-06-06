package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

internal class GetDiaryEntryByIdUseCase(
    private val repository: SkinDiaryRepository
) {
    suspend operator fun invoke(id: String): Result<DiaryEntry> {
        return repository.getEntryById(id)
    }
}