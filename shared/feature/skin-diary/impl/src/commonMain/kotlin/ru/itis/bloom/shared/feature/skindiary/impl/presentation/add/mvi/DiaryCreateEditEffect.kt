package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi

sealed interface DiaryCreateEditEffect {
    data object NavigateBack : DiaryCreateEditEffect
    data object ShowSuccess : DiaryCreateEditEffect
    data class ShowError(val message: String) : DiaryCreateEditEffect
    data class ShowPhotoError(val message: String) : DiaryCreateEditEffect
}