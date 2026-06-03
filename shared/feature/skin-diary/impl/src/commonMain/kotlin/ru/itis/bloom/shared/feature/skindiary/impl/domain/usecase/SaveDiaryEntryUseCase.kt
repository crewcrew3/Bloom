package ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase

import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import ru.itis.bloom.shared.feature.skindiary.impl.domain.model.SaveDiaryEntryCommand

internal class SaveDiaryEntryUseCase(
    private val repository: SkinDiaryRepository
) {
    suspend operator fun invoke(
        command: SaveDiaryEntryCommand
    ): Result<DiaryEntry> {

        val request = CreateDiaryEntryRequest(
            entryDate = command.date.toString(),
            skinCondition = command.skinCondition,
            hydrationLevel = command.hydrationLevel,
            problemZones = ProblemZone.toJson(command.problemZones).takeIf { it.isNotEmpty() },
            notes = command.notes.takeIf { it.isNotBlank() }
        )

        return repository.saveEntry(
            request = request,
            photoBytes = command.photoBytes,
            isUpdate = command.id != null,
            id = command.id
        )
    }
}