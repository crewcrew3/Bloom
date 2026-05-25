package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.components.settings.DiaryCardSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom

@Composable
fun DiaryCard(
    settings: DiaryCardSettings,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(enabled = settings.onClick != {}) { settings.onClick() }
            .then(if (settings.onClick != {}) Modifier else Modifier),
        shape = settings.shape ?: RoundedCornerShape(DimensionsCustom.cardCornerRadius),
        color = settings.containerColor ?: MaterialTheme.colorScheme.surface,
        contentColor = settings.contentColor ?: MaterialTheme.colorScheme.onSurface,
        shadowElevation = settings.elevation.dp,
        tonalElevation = if (settings.containerColor == null) 1.dp else 0.dp
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun CardCustomPreview() {
    BloomTheme {
        DiaryCard(
            settings = DiaryCardSettings.Default,
            modifier = Modifier
        ) {
            Text(
                text = "Card content",
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            )
        }
    }
}