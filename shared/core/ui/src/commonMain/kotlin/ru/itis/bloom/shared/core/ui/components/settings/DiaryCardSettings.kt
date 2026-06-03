package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


data class DiaryCardSettings(
    val onClick: () -> Unit = {},
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val shape: Shape? = null,
    val elevation: Int = 1,

    // Diary-specific fields (optional — null when used as generic card)
    val entryDate: String? = null,
    val skinCondition: Int? = null,
    val hydrationLevel: Int? = null,
    val problemZones: List<String> = emptyList(),
    val notes: String? = null,
    val photoUrl: String? = null,
    val syncStatus: String? = null,
) {
    companion object {
        val Default = DiaryCardSettings()
    }
}