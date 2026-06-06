package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.components.settings.FilterChipSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom

@Composable
fun FilterChipCustom(
    settings: FilterChipSettings,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (settings.isSelected) {
        settings.resolveSelectedColor()
    } else {
        settings.resolveUnselectedColor()
    }
    val textColor = settings.resolveTextColor()
    val borderColor = if (!settings.isSelected) {
        MaterialTheme.colorScheme.outlineVariant
    } else {
        Color.Transparent
    }

    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(DimensionsCustom.chipCornerRadius)
            )
            .clickable { settings.onClick() },
        shape = RoundedCornerShape(DimensionsCustom.chipCornerRadius),
        color = backgroundColor,
        contentColor = textColor
    ) {
        Text(
            text = settings.text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(
                horizontal = DimensionsCustom.chipHorizontalPadding,
                vertical = DimensionsCustom.chipVerticalPadding
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipCustomPreview() {
    BloomTheme {
        FilterChipCustom(
            settings = FilterChipSettings(
                text = "7 days",
                isSelected = true,
                onClick = {}
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}