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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.DiaryCard
import ru.itis.bloom.shared.core.ui.components.StatusBadge
import ru.itis.bloom.shared.core.ui.components.settings.DiaryCardSettings
import ru.itis.bloom.shared.core.ui.model.SyncStatus
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.utils.skinConditionLabel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.utils.toZoneString

@Composable
fun DiaryEntryCard(
    settings: DiaryCardSettings,
    modifier: Modifier = Modifier
) {
    DiaryCard(
        settings = DiaryCardSettings(
            onClick = settings.onClick,
            containerColor = settings.containerColor,
            contentColor = settings.contentColor,
            elevation = 0
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
                placeholderIcon = IconsCustom.iconCamera(),
                model = settings.photoUrl,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = settings.entryDate ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                settings.entryDate?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val conditionLabel = settings.skinCondition?.let {
                    skinConditionLabel(it)
                }
                if (conditionLabel != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = IconsCustom.iconSkinCondition(),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = conditionLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (settings.problemZones.isNotEmpty()) {
                    val zones = settings.problemZones
                        .map { stringResource(it.toZoneString()) }
                        .joinToString(", ")
                    Text(
                        text = zones,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            if (settings.syncStatus == "pending") {
                StatusBadge(SyncStatus.PENDING)
            }
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