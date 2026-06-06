package ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone

data class DiaryDetailState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val entry: DiaryEntry? = null,
    val problemZonesList: ImmutableList<ProblemZone> = persistentListOf(),
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false
)