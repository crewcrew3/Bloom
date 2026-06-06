package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository

internal class SyncPendingDiaryEntriesUseCase (
    private val repository: SkinDiaryRepository
) {
    suspend operator fun invoke() {
        repository.syncPendingEntries()
    }
}