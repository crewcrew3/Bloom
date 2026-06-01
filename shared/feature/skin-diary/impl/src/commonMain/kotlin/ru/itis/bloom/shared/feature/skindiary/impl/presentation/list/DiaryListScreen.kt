package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_action_add
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_empty_subtitle
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_empty_title
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_screen_title
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.components.settings.IconSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.filters.DiaryFiltersRow
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListIntent
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListState

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryListContent(
    state: DiaryListState,
    onIntent: (DiaryListIntent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var showSortDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        DiaryFiltersRow(
            dateRange = state.dateRange,
            sort = state.sort,
            showSortDropdown = showSortDropdown,
            onDateFilterClick = {
                // TODO: открыть DatePicker диалог
            },
            onSortIconClick = { showSortDropdown = true },
            onSortSelected = { order ->
                showSortDropdown = false
                onIntent(DiaryListIntent.ChangeSort(order))
            },
            onSortDismiss = { showSortDropdown = false }
        )

        when {
            state.isLoading && state.entries.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            state.entries.isEmpty() -> {
                DiaryEmptyState(modifier = Modifier.weight(1f))
            }

            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onIntent(DiaryListIntent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    DiaryEntriesList(
                        entries = state.entries,
                        hasMore = state.hasMore,
                        onEntryClick = { id -> onIntent(DiaryListIntent.NavigateToDetail(id)) },
                        onLoadMore = { onIntent(DiaryListIntent.LoadNextPage) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiaryEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = IconsCustom.iconDiaryEmpty(),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.diary_empty_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.diary_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=portrait")
@Composable
private fun DiaryListScreenPreview() {
    MaterialTheme {
        DiaryListScreen(
            state = DiaryListState(
                entries = persistentListOf(
                    DiaryEntry(
                        id = "1",
                        userId = "user1",
                        entryDate = "11.04.2025",
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
                        entryDate = "10.04.2025",
                        skinCondition = 6,
                        hydrationLevel = null,
                        problemZones = null,
                        notes = null,
                        photoUrl = null,
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
