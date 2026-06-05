package ru.itis.bloom.shared.feature.profile.impl.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import bloom.shared.feature.profile.impl.generated.resources.Res
import bloom.shared.feature.profile.impl.generated.resources.common_cancel
import bloom.shared.feature.profile.impl.generated.resources.common_save
import bloom.shared.feature.profile.impl.generated.resources.profile_change_password
import bloom.shared.feature.profile.impl.generated.resources.profile_confirm_password
import bloom.shared.feature.profile.impl.generated.resources.profile_current_password
import bloom.shared.feature.profile.impl.generated.resources.profile_new_password
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.FormField
import ru.itis.bloom.shared.core.ui.components.settings.FormFieldSettings
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

@Composable
internal fun ChangePasswordDialog(
    errorMessage: ProfileMessageRes?,
    onConfirm: (current: String, new: String, confirm: String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.profile_change_password)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FormField(
                    settings = FormFieldSettings(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = Res.string.profile_current_password,
                        singleLine = true
                    )
                )
                FormField(
                    settings = FormFieldSettings(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = Res.string.profile_new_password,
                        singleLine = true
                    )
                )
                FormField(
                    settings = FormFieldSettings(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = Res.string.profile_confirm_password,
                        singleLine = true
                    )
                )
                errorMessage?.let { error ->
                    Text(
                        text = stringResource(error.toResourceId()),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentPassword, newPassword, confirmPassword) }) {
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