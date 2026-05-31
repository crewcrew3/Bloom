package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

internal class SaveDiaryEntryUseCase(
    private val repository: SkinDiaryRepository
) {
    suspend operator fun invoke(
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?,
        isUpdate: Boolean = false,
        id: String? = null
    ): Result<DiaryEntry> {
        return repository.saveEntry(
            request = request,
            photoBytes = photoBytes,
            isUpdate = isUpdate,
            id = id
        )
    }
}