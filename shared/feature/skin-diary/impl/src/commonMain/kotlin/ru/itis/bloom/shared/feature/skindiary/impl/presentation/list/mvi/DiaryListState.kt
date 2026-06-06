package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

data class DiaryListState(
    val entries: ImmutableList<DiaryEntry> = persistentListOf(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val dateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val sort: DiarySortOrder = DiarySortOrder.DATE_DESC,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val error: String? = null
)