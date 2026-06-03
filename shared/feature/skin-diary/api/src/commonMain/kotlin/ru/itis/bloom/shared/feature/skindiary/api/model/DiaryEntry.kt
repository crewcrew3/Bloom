package ru.itis.bloom.shared.feature.skindiary.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntry(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("entry_date")
    val entryDate: String,
    @SerialName("skin_condition")
    val skinCondition: Int,
    @SerialName("hydration_level")
    val hydrationLevel: Int?,
    @SerialName("problem_zones")
    val problemZones: String?,
    @SerialName("notes")
    val notes: String?,
    @SerialName("photo_url")
    val photoUrl: String?,
    @SerialName("sync_status")
    val syncStatus: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)