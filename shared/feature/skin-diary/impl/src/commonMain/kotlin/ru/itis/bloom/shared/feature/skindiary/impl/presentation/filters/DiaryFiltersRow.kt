package ru.itis.bloom.shared.feature.skindiary.impl.presentation.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_filter_placeholder
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_filter_to
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_date_asc
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_date_desc
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_label
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_sort_skin_condition_desc
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.FilterChipCustom
import ru.itis.bloom.shared.core.ui.components.settings.FilterChipSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi.DiarySortOrder

@Composable
fun DiaryFiltersRow(
    dateRange: Pair<LocalDate?, LocalDate?>,
    sort: DiarySortOrder,
    showSortDropdown: Boolean,
    onDateFilterClick: () -> Unit,
    onSortIconClick: () -> Unit,
    onSortSelected: (DiarySortOrder) -> Unit,
    onSortDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = DimensionsCustom.baseInsets, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Иконка фильтра (воронка)
        IconButton(
            onClick = onDateFilterClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = IconsCustom.iconFilter(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Chip с датой
        FilterChipCustom(
            settings = FilterChipSettings(
                text = if (dateRange.first == null)
                    stringResource(Res.string.diary_filter_placeholder)
                else {
                    val to = dateRange.second?.toString()
                        ?: stringResource(Res.string.diary_filter_to)
                    "${dateRange.first} – $to"
                },
                isSelected = dateRange.first != null,
                onClick = onDateFilterClick
            ),
            modifier = Modifier.weight(1f)
        )

        // Иконка сортировки с Dropdown
        Box {
            IconButton(
                onClick = onSortIconClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = IconsCustom.iconSortAsc(),
                    contentDescription = stringResource(Res.string.diary_sort_label),
                    tint = if (sort != DiarySortOrder.DATE_DESC)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            SortDropdownCustom(
                currentSort = sort.apiValue,
                onSortSelected = { option ->
                    val order = DiarySortOrder.entries.find { it.apiValue == option.value }
                        ?: DiarySortOrder.DATE_DESC
                    onSortSelected(order)
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
                expanded = showSortDropdown,
                onDismiss = onSortDismiss,
                label = stringResource(Res.string.diary_sort_label)
            )
        }
    }
}