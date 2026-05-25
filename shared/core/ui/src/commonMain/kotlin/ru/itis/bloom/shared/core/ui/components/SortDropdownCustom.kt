package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.bloom.shared.core.ui.components.settings.FilterChipSettings
import ru.itis.bloom.shared.core.ui.components.settings.SortOption
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun SortDropdownCustom(
    currentSort: String,
    sortOptions: List<SortOption>,
    onSortSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Sort by"
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FilterChipCustom(
            settings = FilterChipSettings(
                text = sortOptions.find { it.value == currentSort }?.label ?: label,
                isSelected = false,
                onClick = { expanded = true }
            ),
            modifier = Modifier
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sortOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSortSelected(option)
                        expanded = false
                    },
                    trailingIcon = {
                        if (option.value == currentSort) {
                            Icon(
                                painter = IconsCustom.iconPlaceholderProduct(),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortDropdownCustomPreview() {
    BloomTheme {
        SortDropdownCustom(
            currentSort = "date_desc",
            sortOptions = listOf(
                SortOption("date_desc", "Newest first"),
                SortOption("date_asc", "Oldest first"),
                SortOption("skin_condition_desc", "By skin condition")
            ),
            onSortSelected = {}
        )
    }
}