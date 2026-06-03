package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun FloatingActionButtonCustom(
    settings: FloatingActionButtonSettings,
) {
    FloatingActionButton(
        onClick = settings.onClick,
        containerColor = settings.containerColor
            ?: MaterialTheme.colorScheme.secondaryContainer,
        contentColor = settings.contentColor
            ?: MaterialTheme.colorScheme.onPrimary,
        shape = settings.shape
            ?: RoundedCornerShape(DimensionsCustom.roundShapeBtn),
        modifier = Modifier
            .padding(
                end = 8.dp,
                bottom = 8.dp
            ),
    ) {
        Icon(
            painter = settings.iconSettings.iconPainter ?: IconsCustom.iconPlus(),
            contentDescription = settings.iconSettings.description,
            modifier = Modifier
                .size(24.dp),
        )
    }
}