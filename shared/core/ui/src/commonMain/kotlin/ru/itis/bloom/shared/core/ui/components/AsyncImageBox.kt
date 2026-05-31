package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun AsyncImageBox(
    model: Any?,
    modifier: Modifier = Modifier,
    placeholderIcon: Painter? = null,
    placeholderDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(12.dp),
    placeholderTint: Color? = null,
) {
    val background = MaterialTheme.colorScheme.surfaceVariant
    if (model != null) {
        AsyncImage(
            model = model,
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
                .clip(shape)
                .background(background)
        )
    } else if (placeholderIcon != null) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = placeholderIcon,
                contentDescription = placeholderDescription,
                tint = placeholderTint ?: MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}