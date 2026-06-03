package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi

import kotlinx.datetime.LocalDate

sealed interface DiaryListIntent {
    data object LoadInitial : DiaryListIntent
    data object Refresh : DiaryListIntent
    data object LoadNextPage : DiaryListIntent
    data class ChangeDateRange(val from: LocalDate?, val to: LocalDate?) : DiaryListIntent
    data class ChangeSort(val order: DiarySortOrder) : DiaryListIntent
    data class NavigateToDetail(val entryId: String) : DiaryListIntent
    data object NavigateToCreate : DiaryListIntent
}