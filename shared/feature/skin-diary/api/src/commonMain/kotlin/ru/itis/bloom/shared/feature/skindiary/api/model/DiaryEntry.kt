package ru.itis.bloom.shared.feature.skindiary.api.model

import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntry(
    val id: String,
    val userId: String,
    val entryDate: String,
    val skinCondition: Int,
    val hydrationLevel: Int?,
    val problemZones: String?,
    val notes: String?,
    val photoUrl: String?,
    val syncStatus: String,
    val createdAt: String,
    val updatedAt: String
)