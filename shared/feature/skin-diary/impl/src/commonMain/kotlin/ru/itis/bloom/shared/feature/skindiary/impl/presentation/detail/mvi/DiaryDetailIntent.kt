package ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi

sealed interface DiaryDetailIntent {
    data object LoadEntry : DiaryDetailIntent
    data object EditEntry : DiaryDetailIntent
    data object NavigateBack : DiaryDetailIntent
    data object DismissError : DiaryDetailIntent
    data object Reload : DiaryDetailIntent
    data object DeleteEntry : DiaryDetailIntent
    data object ConfirmDelete : DiaryDetailIntent
    data object CancelDelete : DiaryDetailIntent
}