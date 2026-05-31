package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.model.SyncStatus

@Composable
fun StatusBadge(
    status: SyncStatus,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp
) {
    val color = when (status) {
        SyncStatus.SYNCED -> MaterialTheme.colorScheme.primary
        SyncStatus.PENDING -> MaterialTheme.colorScheme.tertiary
        SyncStatus.ERROR -> MaterialTheme.colorScheme.error
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}