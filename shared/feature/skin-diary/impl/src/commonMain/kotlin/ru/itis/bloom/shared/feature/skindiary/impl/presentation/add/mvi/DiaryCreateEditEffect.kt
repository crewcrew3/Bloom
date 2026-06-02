package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi

import org.jetbrains.compose.resources.StringResource

sealed interface DiaryCreateEditEffect {
    data object NavigateBack : DiaryCreateEditEffect
    data object ShowSuccess : DiaryCreateEditEffect
    data class ShowError(val message: StringResource) : DiaryCreateEditEffect
    data class ShowPhotoError(val message: StringResource) : DiaryCreateEditEffect
    data class NavigateBackToDetail(val entryId: String) : DiaryCreateEditEffect
}