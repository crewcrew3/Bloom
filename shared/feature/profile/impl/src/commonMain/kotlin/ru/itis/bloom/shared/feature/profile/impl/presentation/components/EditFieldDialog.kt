package ru.itis.bloom.shared.feature.profile.impl.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloom.shared.feature.profile.impl.generated.resources.Res
import bloom.shared.feature.profile.impl.generated.resources.common_cancel
import bloom.shared.feature.profile.impl.generated.resources.common_save
import bloom.shared.feature.profile.impl.generated.resources.profile_field_value
import io.ktor.websocket.Frame
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.FormField
import ru.itis.bloom.shared.core.ui.components.settings.FormFieldSettings
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

@Composable
internal fun EditFieldDialog(
    title: String,
    currentValue: String,
    errorMessage: ProfileMessageRes?,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Frame.Text(title) },
        text = {
            Column {
                FormField(
                    settings = FormFieldSettings(
                        value = text,
                        onValueChange = { text = it },
                        label = Res.string.profile_field_value,
                        singleLine = true
                    )
                )
                errorMessage?.let { error ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(error.toResourceId()),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text(stringResource(Res.string.common_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.common_cancel))
            }
        }
    )
}

