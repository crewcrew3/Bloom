package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.label_notes
import bloom.shared.feature.skin_diary.impl.generated.resources.placeholder_notes
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.FormField
import ru.itis.bloom.shared.core.ui.components.settings.FormFieldSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
internal fun NotesSection(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)) {
        Text(
            text = stringResource(Res.string.label_notes),
            style = StylesCustom.diarySectionLabel.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        FormField(
            settings = FormFieldSettings(
                value = notes,
                onValueChange = onNotesChange,
                label = Res.string.placeholder_notes,
                placeholder = Res.string.placeholder_notes,
                singleLine = false,
            ),
            isFilled = true,
            modifier = Modifier
        )
    }
}