package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.ColorsCustom
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun StarRating(
    rating: Int?,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    filledColor: Color = MaterialTheme.colorScheme.error,
    emptyColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
) {
    Row(modifier = modifier) {
        repeat(maxStars) { index ->
            Icon(
                painter = if (rating != null && index < rating) {
                    IconsCustom.iconStarFilled()
                } else {
                    IconsCustom.iconStarOutline()
                },
                contentDescription = null,
                tint = if (rating != null && index < rating) filledColor else emptyColor,
                modifier = Modifier.size(DimensionsCustom.starSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
private fun StarRatingPreview() {
    BloomTheme {
        StarRating(rating = 3)
    }
}