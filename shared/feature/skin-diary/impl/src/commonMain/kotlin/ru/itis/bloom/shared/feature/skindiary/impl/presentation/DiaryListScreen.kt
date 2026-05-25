package ru.itis.bloom.shared.feature.skindiary.impl.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_action_add
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_action_refresh
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_empty_subtitle
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_empty_title
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_filter_placeholder
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_filter_to
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_form_hydration
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_screen_title
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_date_asc
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_date_desc
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_label
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_skin_condition_desc
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_status_pending
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_status_synced
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
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.DiaryCard
import ru.itis.bloom.shared.core.ui.components.FilterChipCustom
import ru.itis.bloom.shared.core.ui.components.SortDropdownCustom
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.DiaryCardSettings
import ru.itis.bloom.shared.core.ui.components.settings.FilterChipSettings
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.components.settings.IconSettings
import ru.itis.bloom.shared.core.ui.components.settings.SortOption
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.impl.mvi.DiaryListIntent
import ru.itis.bloom.shared.feature.skindiary.impl.mvi.DiaryListState
import ru.itis.bloom.shared.feature.skindiary.impl.mvi.DiarySortOrder

@Composable
internal fun DiaryListScreen(
    state: DiaryListState,
    onIntent: (DiaryListIntent) -> Unit,
    bottomBarSettings: BottomBarSettings,
    modifier: Modifier = Modifier
) {
    BaseScreen(
        topBarSettings = TopBarSettings(
            text = stringResource(Res.string.diary_screen_title)
        ),
        floatActBtnSettings = FloatingActionButtonSettings(
            onClick = { onIntent(DiaryListIntent.NavigateToCreate) },
            iconSettings = IconSettings(
                iconPainter = IconsCustom.iconPlus(),
                description = stringResource(Res.string.diary_action_add)
            )
        ),
        bottomBarSettings = bottomBarSettings,
        content = { paddingValues ->
            DiaryListContent(
                state = state,
                onIntent = onIntent,
                contentPadding = paddingValues,
                modifier = modifier
            )
        }
    )
}

@Composable
private fun DiaryListContent(
    state: DiaryListState,
    onIntent: (DiaryListIntent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        DiaryFiltersRow(
            dateRange = state.dateRange,
            sort = state.sort,
            onDateRangeChange = { from, to -> onIntent(DiaryListIntent.ChangeDateRange(from, to)) },
            onSortChange = { onIntent(DiaryListIntent.ChangeSort(it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.isLoading && state.entries.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.entries.isEmpty() -> {
                DiaryEmptyState(
                    onRefresh = { onIntent(DiaryListIntent.Refresh) },
                    modifier = Modifier.weight(1f)
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.entries,
                        key = { it.id }
                    ) { entry ->
                        DiaryEntryCard(
                            settings = entry.toDiaryCardSettings(
                                onClick = { onIntent(DiaryListIntent.NavigateToDetail(entry.id)) }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (state.hasMore) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(strokeWidth = 2.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== FILTERS ROW ====================
@Composable
private fun DiaryFiltersRow(
    dateRange: Pair<LocalDate?, LocalDate?>,
    sort: DiarySortOrder,
    onDateRangeChange: (LocalDate?, LocalDate?) -> Unit,
    onSortChange: (DiarySortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DimensionsCustom.baseInsets, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipCustom(
            settings = FilterChipSettings(
                text = if (dateRange.first == null)
                    stringResource(Res.string.diary_filter_placeholder)
                else "${dateRange.first} – ${dateRange.second ?: stringResource(Res.string.diary_filter_to)}",
                isSelected = dateRange.first != null,
                onClick = {
                    // TODO: Открыть DatePicker диалог
                    println("Open date picker")
                }
            )
        )

        SortDropdownCustom(
            currentSort = sort.apiValue,
            onSortSelected = { order ->
                onSortChange(DiarySortOrder.entries.find { it.apiValue == order.value }
                    ?: DiarySortOrder.DATE_DESC)
            },
            sortOptions = DiarySortOrder.entries.map {
                SortOption(
                    value = it.apiValue,
                    label = when (it) {
                        DiarySortOrder.DATE_DESC -> stringResource(Res.string.diary_sort_date_desc)
                        DiarySortOrder.DATE_ASC -> stringResource(Res.string.diary_sort_date_asc)
                        DiarySortOrder.SKIN_CONDITION_DESC -> stringResource(Res.string.diary_sort_skin_condition_desc)
                    }
                )
            },
            modifier = Modifier,
            label = stringResource(Res.string.diary_sort_label)
        )
    }
}

// ==================== COMPONENTS ====================
@Composable
internal fun DiaryEntryCard(
    settings: DiaryCardSettings,
    modifier: Modifier = Modifier
) {
    DiaryCard(
        settings = DiaryCardSettings(
            onClick = settings.onClick,
            containerColor = settings.containerColor,
            contentColor = settings.contentColor
        ),
        modifier = modifier.padding(horizontal = DimensionsCustom.baseInsets)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: дата + статус
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 ${settings.entryDate}",
                    style = MaterialTheme.typography.labelMedium
                )
                SyncStatusChip(status = settings.syncStatus)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Основное: состояние кожи
            Text(
                text = "🌡 ${settings.skinCondition}/10",
                style = MaterialTheme.typography.bodyLarge
            )

            // Опционально: увлажнение
            settings.hydrationLevel?.let { level ->
                Text(
                    text = "💧 ${stringResource(Res.string.diary_form_hydration)}: $level/5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Заметки
            settings.notes?.takeIf { it.isNotBlank() }?.let { note ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📝 $note",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            // Проблемные зоны (если есть)
            if (settings.problemZones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                val zoneResources = settings.problemZones.map { it.toZoneString() }

                // 2. Преобразуем StringResource в обычные Strings и объединяем
                // Важно: map выполняется в @Composable контексте, поэтому stringResource работает
                val joinedZones = zoneResources
                    .map { stringResource(it) }
                    .joinToString(", ")

                // 2. Затем объединяем их в одну строку через stringResource
                Text(
                    text = "🎯 $joinedZones",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SyncStatusChip(status: String?) {
    val (textRes, color) = when (status) {
        "pending" -> Res.string.diary_status_pending to MaterialTheme.colorScheme.tertiary
        else -> Res.string.diary_status_synced to MaterialTheme.colorScheme.primary
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.1f),
        contentColor = color
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun DiaryEmptyState(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📭",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.diary_empty_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.diary_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRefresh,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(stringResource(Res.string.diary_action_refresh))
        }
    }
}

// ==================== SETTINGS & MAPPERS ====================


internal fun DiaryEntry.toDiaryCardSettings(onClick: () -> Unit): DiaryCardSettings {
    return DiaryCardSettings(
        entryDate = entryDate,
        skinCondition = skinCondition,
        hydrationLevel = hydrationLevel,
        problemZones = problemZones?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
            ?: emptyList(),
        notes = notes,
        syncStatus = syncStatus,
        onClick = onClick
    )
}

private fun String.toZoneString(): StringResource {
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

// ==================== PREVIEW ====================
@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=portrait")
@Composable
private fun DiaryListScreenPreview() {
    MaterialTheme {
        DiaryListScreen(
            state = DiaryListState(
                entries = listOf(
                    DiaryEntry(
                        id = "1",
                        userId = "user1",
                        entryDate = "2024-05-20",
                        skinCondition = 8,
                        hydrationLevel = 4,
                        problemZones = "forehead,nose",
                        notes = "Немного сухости после пилинга",
                        photoUrl = null,
                        syncStatus = "synced",
                        createdAt = "",
                        updatedAt = ""
                    ),
                    DiaryEntry(
                        id = "2",
                        userId = "user1",
                        entryDate = "2024-05-19",
                        skinCondition = 6,
                        hydrationLevel = null,
                        problemZones = null,
                        notes = null,
                        photoUrl = "https://via.placeholder.com/100",
                        syncStatus = "pending",
                        createdAt = "",
                        updatedAt = ""
                    )
                )
            ),
            onIntent = {},
            bottomBarSettings = BottomBarSettings({}, {}, {}, {})
        )
    }
}