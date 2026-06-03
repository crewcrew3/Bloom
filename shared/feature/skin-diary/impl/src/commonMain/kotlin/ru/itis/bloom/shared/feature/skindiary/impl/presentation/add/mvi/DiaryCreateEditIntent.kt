package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi

import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone

sealed interface DiaryCreateEditIntent {
    data class SetDate(val date: LocalDate) : DiaryCreateEditIntent
    data class SetSkinCondition(val value: Int) : DiaryCreateEditIntent // 1-10
    data class SetHydrationLevel(val value: Int) : DiaryCreateEditIntent // 1-5
    data class ToggleProblemZone(val zone: ProblemZone) : DiaryCreateEditIntent
    data class SetNotes(val notes: String) : DiaryCreateEditIntent
    data class RequestPhotoSelection(val uri: String) : DiaryCreateEditIntent
    data object PhotoProcessingStarted : DiaryCreateEditIntent
    data class PhotoProcessingFinished(val photoBytes: ByteArray) : DiaryCreateEditIntent
    data class PhotoProcessingError(val message: String) : DiaryCreateEditIntent
    data object RemovePhoto : DiaryCreateEditIntent
    data object SaveEntry : DiaryCreateEditIntent
    data object NavigateBack : DiaryCreateEditIntent
}