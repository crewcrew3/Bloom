package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.label_excellent
import bloom.shared.feature.skin_diary.impl.generated.resources.label_poor
import bloom.shared.feature.skin_diary.impl.generated.resources.label_skin_condition
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
internal fun SkinConditionSection(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diarySliderLabelSpacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.label_skin_condition),
                style = StylesCustom.diarySectionLabel.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = value.toString(),
                style = StylesCustom.diarySliderValue.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(DimensionsCustom.diaryCornerRadiusSmall)
                    )
                    .padding(
                        horizontal = DimensionsCustom.diaryBadgeHorizontalPadding,
                        vertical = DimensionsCustom.diaryBadgeVerticalPadding
                    )
            )
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 1f..10f,
            steps = 8,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.label_poor),
                style = StylesCustom.diarySliderLabel.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = stringResource(Res.string.label_excellent),
                style = StylesCustom.diarySliderLabel.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}