package ru.itis.bloom.shared.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
fun CollapsibleTextSection(
    title: String,
    content: String?,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLinesCollapsed: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }
    val displayContent = content?.takeIf { it.isNotBlank() } ?: placeholder

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = StylesCustom.sectionTitle,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = IconsCustom.iconExpand(),
                contentDescription = if (isExpanded) stringResource(Res.string.collaps_section_content_desc_icon_expand_less) else stringResource(Res.string.collaps_section_content_desc_icon_expand_more),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(DimensionsCustom.productDetailExpandIconSize)
                    .padding(start = 8.dp)
                    .rotate(if (isExpanded) 180f else 0f)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Text(
                text = displayContent,
                style = StylesCustom.sectionContent,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun CollapsibleTextSectionPreview() {
    BloomTheme {
        CollapsibleTextSection(
            title = "Состав",
            content = "Aqua, Glycerin, Niacinamide, Panthenol, Sodium Hyaluronate",
            placeholder = "Неизвестно"
        )
    }
}