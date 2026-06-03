package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

@Suppress("FrequentlyChangingValue")
@Composable
fun DiaryEntriesList(
    entries: List<DiaryEntry>,
    hasMore: Boolean,
    isLoading: Boolean,
    onEntryClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    // Состояние для хранения позиции центра списка в пикселях
    var listCenterY by remember { mutableStateOf(0f) }

    val focusScales = remember(entries.size, listState.firstVisibleItemScrollOffset, listCenterY) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo

            visibleItems.associate { itemInfo ->
                val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
                val distanceToCenter = kotlin.math.abs(itemCenter - listCenterY)

                val maxDistance = with(density) { 250.dp.toPx() }
                val normalizedDist = (distanceToCenter / maxDistance).coerceIn(0f, 1f)

                val scale = 1.05f - (normalizedDist * 0.05f)

                itemInfo.index to scale
            }
        }
    }
    LaunchedEffect(entries.size, entries.firstOrNull()?.id) {
        listState.animateScrollToItem(0)
    }
    // Пагинация: триггер за 3 элемента до конца
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            hasMore && lastVisible >= total - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .onPlaced { coordinates ->
                listCenterY = coordinates.size.height / 2f
            },
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        // Показываем Shimmer при первой загрузке
        if (isLoading && entries.isEmpty()) {
            items(count = 8) {
                DiaryEntryCardShimmer(modifier = Modifier.fillMaxWidth())
            }
        } else {
            items(
                items = entries,
                key = { it.id },
                contentType = { "diary_entry" }
            ) { entry ->
                val currentIndex = entries.indexOf(entry)
                val currentScale = focusScales.value[currentIndex] ?: 1f

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DiaryEntryCard(
                        entry = entry,
                        onClick = { onEntryClick(entry.id) },
                        photoScale = currentScale,
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(currentScale)
                    )
                }
            }
        }

        if (hasMore && !isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
