package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.BorderStroke
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
import ru.itis.bloom.shared.core.ui.components.settings.CategoryChipSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
fun CategoryChip(
    settings: CategoryChipSettings,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (settings.isSelected) {
        settings.selectedColor ?: MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val borderColor = if (settings.isSelected) {
        Color.Transparent
    } else {
        settings.unselectedBorderColor ?: MaterialTheme.colorScheme.primaryContainer
    }
    val textColor = if (settings.isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        settings.textColor ?: MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(DimensionsCustom.categoryChipRadius),
        color = backgroundColor,
        border = BorderStroke(DimensionsCustom.categoryChipBorderWidth, borderColor),
        modifier = modifier
            .clickable(onClick = settings.onClick)
            .padding(horizontal = DimensionsCustom.categoryChipHorizontalPadding)
    ) {
        Text(
            text = settings.text,
            style = StylesCustom.categoryChipText,
            color = textColor,
            modifier = Modifier.padding(
                vertical = DimensionsCustom.categoryChipVerticalPadding,
                horizontal = DimensionsCustom.categoryChipTextHorizontalPadding
            )
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun CategoryChipPreview() {
    BloomTheme {
        CategoryChip(
            settings = CategoryChipSettings(
                text = "Cleanser",
                isSelected = true
            )
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun CategoryChipUnselectedPreview() {
    BloomTheme {
        CategoryChip(
            settings = CategoryChipSettings(
                text = "Serum",
                isSelected = false
            )
        )
    }
}