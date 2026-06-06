package ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.btn_cancel
import bloom.shared.feature.skin_diary.impl.generated.resources.btn_delete
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_action_delete
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_action_edit
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_delete_confirmation
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_delete_title
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_detail_title
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_hydration_label
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_notes_label
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_problem_zones_label
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_skin_condition_label
import bloom.shared.feature.skin_diary.impl.generated.resources.error_loading_entry
import bloom.shared.feature.skin_diary.impl.generated.resources.hydration_dry
import bloom.shared.feature.skin_diary.impl.generated.resources.hydration_moisturized
import bloom.shared.feature.skin_diary.impl.generated.resources.hydration_normal
import bloom.shared.feature.skin_diary.impl.generated.resources.hydration_very_dry
import bloom.shared.feature.skin_diary.impl.generated.resources.hydration_very_moisturized
import bloom.shared.feature.skin_diary.impl.generated.resources.retry
import bloom.shared.feature.skin_diary.impl.generated.resources.skin_condition_bad
import bloom.shared.feature.skin_diary.impl.generated.resources.skin_condition_excellent
import bloom.shared.feature.skin_diary.impl.generated.resources.skin_condition_good
import bloom.shared.feature.skin_diary.impl.generated.resources.skin_condition_normal
import bloom.shared.feature.skin_diary.impl.generated.resources.skin_condition_poor
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.BurgerMenuSettings
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.components.settings.IconSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.ColorsCustom
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailIntent
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailState
import java.text.SimpleDateFormat
import java.util.Locale

private const val HEADER_HEIGHT_FRACTION = 0.5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDetailScreen(
    state: DiaryDetailState,
    onIntent: (DiaryDetailIntent) -> Unit,
    bottomBarSettings: BottomBarSettings?,
    topBarSettings: TopBarSettings?,
    burgerMenuSettings: BurgerMenuSettings?,
    modifier: Modifier = Modifier
) {
    BaseScreen(
        topBarSettings = topBarSettings?.copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        bottomBarSettings = bottomBarSettings,
        burgerMenuSettings = burgerMenuSettings,
        floatActBtnSettings = FloatingActionButtonSettings(
            onClick = { onIntent(DiaryDetailIntent.EditEntry) },
            iconSettings = IconSettings(
                iconPainter = IconsCustom.iconEdit(),
                description = stringResource(Res.string.diary_action_edit)
            )
        )
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.isError) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = state.errorMessage ?: stringResource(Res.string.error_loading_entry),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { onIntent(DiaryDetailIntent.LoadEntry) }) {
                        Text(stringResource(Res.string.retry))
                    }
                }
            }
        } else {
            state.entry?.let { entry ->
                DiaryDetailContent(
                    entry = entry,
                    problemZones = state.problemZonesList,
                    isDeleting = state.isDeleting,
                    onIntent = onIntent,
                    contentPadding = paddingValues,
                    modifier = modifier
                )
            }
        }
        if (state.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = { onIntent(DiaryDetailIntent.ConfirmDelete) },
                onDismiss = { onIntent(DiaryDetailIntent.CancelDelete) }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.diary_delete_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.diary_delete_confirmation),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(Res.string.btn_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        }
    )
}

@Composable
private fun DiaryDetailContent(
    entry: DiaryEntry,
    problemZones: List<ProblemZone>,
    isDeleting: Boolean,
    onIntent: (DiaryDetailIntent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val headerColor = MaterialTheme.colorScheme.secondaryContainer
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val brush = Brush.verticalGradient(
                    colors = listOf(
                        headerColor,
                        Color.Transparent
                    ),
                    startY = 40f,
                    endY = (size.height * HEADER_HEIGHT_FRACTION).toFloat()
                )
                drawRect(brush = brush)
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = DimensionsCustom.baseInsets)
                .padding(bottom = 64.dp),
            verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diarySectionSpacing)
        ) {
            // Photo Section (top centered)
            entry.photoUrl?.let { url ->
                AsyncImageBox(
                    model = url,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensionsCustom.diaryPhotoHeight),
                    placeholderIcon = IconsCustom.iconCamera(),
                    contentScale = ContentScale.Crop,
                    shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusLarge)
                )
            }

            // Date
            DateSection(date = entry.entryDate)

            // Skin Condition (1-10)
            SkinConditionSection(value = entry.skinCondition)

            // Hydration Level (1-5)
            entry.hydrationLevel?.let { level ->
                HydrationLevelSection(level = level)
            }

            // Problem Zones
            if (problemZones.isNotEmpty()) {
                ProblemZonesSection(zones = problemZones)
            }

            // Notes
            entry.notes?.let { notes ->
                if (notes.isNotBlank()) {
                    NotesSection(notes = notes)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = DimensionsCustom.baseInsets,
                    vertical = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDeleting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                OutlinedButton(
                    onClick = { onIntent(DiaryDetailIntent.DeleteEntry) },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        painter = IconsCustom.iconDelete(),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.diary_action_delete))
                }
            }
        }
    }
}

@Composable
private fun DateSection(date: String) {
    val formattedDate = formatDate(date)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DimensionsCustom.diaryDateFieldPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = IconsCustom.iconCalendar(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(DimensionsCustom.diaryIconSizeMedium)
            )
            Spacer(modifier = Modifier.width(DimensionsCustom.diaryFieldSpacing))
            Text(
                text = formattedDate,
                style = StylesCustom.diaryDateText,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SkinConditionSection(value: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Заголовок с иконкой
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = IconsCustom.iconSkinCondition(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(Res.string.diary_skin_condition_label),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Большая цифра
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Прогресс-бар
            LinearProgressIndicator(
                progress = { value / 10f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getSkinConditionColor(value),
                trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            )

            // Подписи шкалы
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "1",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "10",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            // Текстовое описание состояния
            Text(
                text = getSkinConditionText(value),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getSkinConditionColor(value: Int): Color {
    return when {
        value >= 9 -> ColorsCustom.SkinConditionExcellent // Зелёный — отличное
        value >= 7 -> ColorsCustom.SkinConditionGood  // Салатовый — хорошее
        value >= 5 -> ColorsCustom.SkinConditionNormal  // Жёлтый — среднее
        value >= 3 -> ColorsCustom.SkinConditionPoor  // Оранжевый — проблемы
        else -> ColorsCustom.SkinConditionBad        // Красный — плохое
    }
}

@Composable
private fun getSkinConditionText(value: Int): String {
    return when {
        value <= 2 -> stringResource(Res.string.skin_condition_bad)
        value <= 4 -> stringResource(Res.string.skin_condition_poor)
        value <= 6 -> stringResource(Res.string.skin_condition_normal)
        value <= 8 -> stringResource(Res.string.skin_condition_good)
        else -> stringResource(Res.string.skin_condition_excellent)
    }
}

@Composable
private fun HydrationLevelSection(level: Int) {
    val hydrationText = when (level) {
        1 -> stringResource(Res.string.hydration_very_dry)
        2 -> stringResource(Res.string.hydration_dry)
        3 -> stringResource(Res.string.hydration_normal)
        4 -> stringResource(Res.string.hydration_moisturized)
        5 -> stringResource(Res.string.hydration_very_moisturized)
        else -> "-"
    }

    InfoSection(
        title = stringResource(Res.string.diary_hydration_label),
        value = hydrationText
    )
}

@Composable
private fun InfoSection(title: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)
    ) {
        Text(
            text = title,
            style = StylesCustom.diarySectionLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusSmall),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(
                    horizontal = DimensionsCustom.diaryBadgeHorizontalPadding,
                    vertical = DimensionsCustom.diaryBadgeVerticalPadding
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun ProblemZonesSection(zones: List<ProblemZone>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)
    ) {
        Text(
            text = stringResource(Res.string.diary_problem_zones_label),
            style = StylesCustom.diarySectionLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryChipSpacing),
            verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryChipSpacing)
        ) {
            zones.forEach { zone ->
                Surface(
                    shape = RoundedCornerShape(DimensionsCustom.chipCornerRadius),
                    color = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    Text(
                        text = zone.displayName,
                        modifier = Modifier.padding(
                            horizontal = DimensionsCustom.chipHorizontalPadding,
                            vertical = DimensionsCustom.chipVerticalPadding
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun NotesSection(notes: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)
    ) {
        Text(
            text = stringResource(Res.string.diary_notes_label),
            style = StylesCustom.diarySectionLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusMedium),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = notes,
                modifier = Modifier.padding(DimensionsCustom.diaryDateFieldPadding),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDate(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val date = inputFormat.parse(dateStr)
        date?.let { outputFormat.format(it) } ?: dateStr
    } catch (e: Exception) {
        dateStr
    }
}

// Preview
@Composable
@Preview
private fun DiaryDetailScreenPreview() {
    BloomTheme {
        DiaryDetailScreen(
            state = DiaryDetailState(
                isLoading = false,
                entry = DiaryEntry(
                    id = "1",
                    userId = "user1",
                    entryDate = "2026-01-09",
                    skinCondition = 7,
                    hydrationLevel = 2,
                    problemZones = "[\"forehead\",\"chin\"]",
                    notes = "Сегодня был хороший день",
                    photoUrl = null,
                    syncStatus = "synced",
                    createdAt = "",
                    updatedAt = ""
                ),
                problemZonesList = persistentListOf(
                    ProblemZone.FOREHEAD,
                    ProblemZone.CHIN
                )
            ),
            onIntent = {},
            bottomBarSettings = null,
            topBarSettings = TopBarSettings(
                text = stringResource(Res.string.diary_detail_title),
                iconType = TopBarIconType.BACK,
                onIconClick = {}
            ),
            burgerMenuSettings = null
        )
    }
}