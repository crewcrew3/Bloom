package ru.itis.bloom.shared.feature.skindiary.impl.presentation.utils

import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_chin
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_forehead
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_jawline
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_left_cheek
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_neck
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_nose
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_other
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_right_cheek
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_t_zone
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_zone_under_eyes
import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.core.ui.components.settings.DiaryCardSettings
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

fun DiaryEntry.toDiaryCardSettings(onClick: () -> Unit): DiaryCardSettings {
    return DiaryCardSettings(
        entryDate = entryDate,
        skinCondition = skinCondition,
        hydrationLevel = hydrationLevel,
        problemZones = problemZones
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList(),
        notes = notes,
        photoUrl = photoUrl,
        syncStatus = syncStatus,
        onClick = onClick
    )
}

fun skinConditionLabel(value: Int): String {
    return when (value) {
        in 1..3 -> "Плохое состояние"
        in 4..5 -> "Удовлетворительное"
        in 6..7 -> "Хорошее состояние"
        in 8..9 -> "Отличное состояние"
        10 -> "Идеальное"
        else -> "$value/10"
    }
}

fun String.toZoneString(): StringResource {
    return when (this) {
        "forehead" -> Res.string.diary_zone_forehead
        "nose" -> Res.string.diary_zone_nose
        "chin" -> Res.string.diary_zone_chin
        "left_cheek" -> Res.string.diary_zone_left_cheek
        "right_cheek" -> Res.string.diary_zone_right_cheek
        "t_zone" -> Res.string.diary_zone_t_zone
        "jawline" -> Res.string.diary_zone_jawline
        "neck" -> Res.string.diary_zone_neck
        "under_eyes" -> Res.string.diary_zone_under_eyes
        else -> Res.string.diary_zone_other
    }
}