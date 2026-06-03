package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import kotlin.time.Clock

data class DiaryCreateEditState(
    val isLoading: Boolean = false,
    val isPhotoProcessing: Boolean = false,
    val entryId: String? = null,
    val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val skinCondition: Int = 5, // 1-10, по умолчанию 5
    val hydrationLevel: Int = 3, // 1-5, по умолчанию 3
    val problemZones: List<ProblemZone> = emptyList(),
    val notes: String = "",
    val photoBytes: ByteArray? = null,
    val photoUrl: String? = null,
    val photoError: String? = null,
    val isError: Boolean = false,
    val errorMessage: String? = null
)