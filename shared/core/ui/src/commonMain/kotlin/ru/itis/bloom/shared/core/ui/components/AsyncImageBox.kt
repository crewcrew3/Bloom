package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    val background = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f)
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
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                painter = placeholderIcon,
                contentDescription = placeholderDescription,
                tint = placeholderTint?.copy(0.5f)
                    ?: MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}