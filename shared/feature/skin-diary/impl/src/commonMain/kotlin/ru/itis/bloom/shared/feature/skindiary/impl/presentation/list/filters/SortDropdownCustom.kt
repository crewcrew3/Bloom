package ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.filters

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun SortDropdownCustom(
    currentSort: String,
    sortOptions: List<SortOption>,
    onSortSelected: (SortOption) -> Unit,
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Sort by"
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        sortOptions.forEach { option ->
            DropdownMenuItem(
                text = { Text(option.label) },
                onClick = {
                    onSortSelected(option)
                    onDismiss()
                },
                trailingIcon = {
                    if (option.value == currentSort) {
                        Icon(
                            painter = IconsCustom.iconCheck(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
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
            onSortSelected = {},
            expanded = true,
            onDismiss = {},
            modifier = Modifier,
            label = "Sort by"
        )
    }
}