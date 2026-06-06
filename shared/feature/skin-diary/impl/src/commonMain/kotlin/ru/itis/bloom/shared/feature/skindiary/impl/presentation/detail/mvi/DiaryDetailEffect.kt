package ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi

import org.jetbrains.compose.resources.StringResource

sealed interface DiaryDetailEffect {
    data class NavigateToEdit(val entryId: String) : DiaryDetailEffect
    data object NavigateBack : DiaryDetailEffect
    data class ShowError(val message: StringResource) : DiaryDetailEffect
    data class ShowSuccess(val message: StringResource) : DiaryDetailEffect
}