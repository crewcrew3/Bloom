package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.DiaryCard
import ru.itis.bloom.shared.core.ui.components.ShimmerEffect
import ru.itis.bloom.shared.core.ui.components.StatusBadge
import ru.itis.bloom.shared.core.ui.components.settings.DiaryCardSettings
import ru.itis.bloom.shared.core.ui.model.SyncStatus
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.utils.skinConditionLabel

@Composable
fun DiaryEntryCard(
    entry: DiaryEntry,
    photoScale: Float = 1f,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DiaryCard(
        settings = DiaryCardSettings(
            onClick = onClick,
            elevation = 0,
            containerColor = Color.Unspecified
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimensionsCustom.baseInsets, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageBox(
                model = entry.photoUrl,
                placeholderIcon = IconsCustom.iconPerson(),
                placeholderTint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .size(120.dp)
                    .scale(photoScale)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Дата
                Text(
                    text = "Дата: ${entry.entryDate}",
                    style = StylesCustom.diarySectionLabel,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Состояние кожи
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = IconsCustom.iconSkinCondition(),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Состояние: ${skinConditionLabel(entry.skinCondition)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Статус синхронизации
            if (entry.syncStatus == "pending") {
                StatusBadge(SyncStatus.PENDING)
            }
        }

        // Разделитель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = DimensionsCustom.baseInsets)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        )
    }
}

@Composable
fun DiaryEntryCardShimmer(
    modifier: Modifier = Modifier
) {
    DiaryCard(
        settings = DiaryCardSettings(
            elevation = 0,
            containerColor = Color.Unspecified
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimensionsCustom.baseInsets, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerEffect(modifier = Modifier.size(120.dp))

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerEffect(modifier = Modifier.height(16.dp).fillMaxWidth(0.7f))
                ShimmerEffect(modifier = Modifier.height(12.dp).fillMaxWidth(0.5f))
                ShimmerEffect(modifier = Modifier.height(12.dp).fillMaxWidth(0.6f))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = DimensionsCustom.baseInsets)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        )
    }
}