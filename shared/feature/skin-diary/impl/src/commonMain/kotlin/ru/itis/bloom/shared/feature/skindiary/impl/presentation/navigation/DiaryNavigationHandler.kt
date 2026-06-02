package ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation

import ru.itis.bloom.shared.core.navigation.api.DiaryNavigator
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListEffect

class DiaryNavigationHandler(
    private val navigator: DiaryNavigator
) {
    fun handleEffect(effect: DiaryListEffect) {
        when (effect) {
            is DiaryListEffect.NavigateToDetail -> navigator.toDiaryDetail(effect.entryId)
            DiaryListEffect.NavigateToCreate -> navigator.toDiaryCreate()
            is DiaryListEffect.ShowError -> { /* TODO: показать Snackbar через UI-слой */ }
        }
    }
    fun handleCreateEditEffect(effect: DiaryCreateEditEffect) {
        when (effect) {
            is DiaryCreateEditEffect.NavigateBack -> navigator.back()
            is DiaryCreateEditEffect.NavigateBackToDetail -> {
                navigator.back()
            }
            else -> {}
        }
    }
    fun handleDetailEffect(effect: DiaryDetailEffect) {
        when (effect) {
            is DiaryDetailEffect.NavigateToEdit -> navigator.toDiaryEdit(effect.entryId)
            DiaryDetailEffect.NavigateBack -> navigator.back()
            else -> {}
        }
    }
}