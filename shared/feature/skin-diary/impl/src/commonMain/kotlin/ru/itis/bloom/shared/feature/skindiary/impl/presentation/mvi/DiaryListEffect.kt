package ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi

import org.jetbrains.compose.resources.StringResource

sealed interface DiaryListEffect {
    data class ShowError(val messageRes: StringResource) : DiaryListEffect
    data object NavigateToCreate : DiaryListEffect
    data class NavigateToDetail(val entryId: String) : DiaryListEffect
}