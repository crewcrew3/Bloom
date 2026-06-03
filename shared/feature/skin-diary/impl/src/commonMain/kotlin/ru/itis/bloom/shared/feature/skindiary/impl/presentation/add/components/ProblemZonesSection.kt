package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.label_problem_zones
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.CategoryChip
import ru.itis.bloom.shared.core.ui.components.settings.CategoryChipSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone

@Composable
internal fun ProblemZonesSection(
    selectedZones: List<ProblemZone>,
    onZoneToggle: (ProblemZone) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)) {
        Text(
            text = stringResource(Res.string.label_problem_zones),
            style = StylesCustom.diarySectionLabel.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryChipSpacing),
            verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryChipSpacing)
        ) {
            ProblemZone.entries.forEach { zone ->
                CategoryChip(
                    settings = CategoryChipSettings(
                        text = zone.displayName,
                        isSelected = selectedZones.contains(zone),
                        onClick = { onZoneToggle(zone) },
                    )
                )
            }
        }
    }
}
