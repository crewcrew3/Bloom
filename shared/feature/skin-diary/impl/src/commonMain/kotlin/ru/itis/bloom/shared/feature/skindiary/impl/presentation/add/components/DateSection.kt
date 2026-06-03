package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.label_date
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
internal fun DateSection(
    date: LocalDate,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)) {
        Text(
            text = stringResource(Res.string.label_date),
            style = StylesCustom.diarySectionLabel.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(DimensionsCustom.diaryCornerRadiusMedium),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DimensionsCustom.diaryDateFieldPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryChipSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = IconsCustom.iconCalendar(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(DimensionsCustom.diaryIconSizeMedium)
                    )
                    Text(
                        text = date.toString(),
                        style = StylesCustom.diaryDateText.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                Icon(
                    painter = IconsCustom.iconChevronRight(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(DimensionsCustom.diaryIconSizeMedium)
                )
            }
        }
    }
}