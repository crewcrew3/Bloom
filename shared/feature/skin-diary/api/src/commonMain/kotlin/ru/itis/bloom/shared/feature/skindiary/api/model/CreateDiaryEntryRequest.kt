package ru.itis.bloom.shared.feature.skindiary.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateDiaryEntryRequest(
    val entryDate: String,
    val skinCondition: Int,
    val hydrationLevel: Int?,
    val problemZones: String?,
    val notes: String?
)