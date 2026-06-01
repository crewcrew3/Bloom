package ru.itis.bloom.shared.feature.skindiary.impl.domain.model

import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone

data class SaveDiaryEntryCommand(
    val id: String?,
    val date: LocalDate,
    val skinCondition: Int,
    val hydrationLevel: Int,
    val problemZones: List<ProblemZone>,
    val notes: String,
    val photoBytes: ByteArray?
)