package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.settings.FormFieldSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom

@Composable
fun FormField(
    settings: FormFieldSettings,
    modifier: Modifier = Modifier,
    isFilled: Boolean = false
) {
    if (isFilled) {
        TextField(
            value = settings.value,
            onValueChange = settings.onValueChange,
            label = { Text(stringResource(settings.label)) },
            placeholder = settings.placeholder?.let { { Text(stringResource(it)) } },
            trailingIcon = settings.trailingIcon?.let { icon ->
                {
                    Icon(
                        painter = icon,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .size(DimensionsCustom.textFieldTrailingIconSize)
                            .then(
                            if (settings.onTrailingIconClick != null) {
                                Modifier.clickable(onClick = settings.onTrailingIconClick)
                            } else {
                                Modifier
                            }
                        )
                    )
                }
            },
            singleLine = settings.singleLine,
            enabled = settings.enabled,
            keyboardOptions = settings.keyboardOptions ?: KeyboardOptions.Default,
            visualTransformation = settings.visualTransformation ?: VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.7f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = modifier.fillMaxWidth()
        )
    } else {
        OutlinedTextField(
            value = settings.value,
            onValueChange = settings.onValueChange,
            label = { Text(stringResource(settings.label)) },
            placeholder = settings.placeholder?.let { { Text(stringResource(it)) } },
            trailingIcon = settings.trailingIcon?.let { icon ->
                {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(DimensionsCustom.textFieldTrailingIconSize)
                            .then(
                            if (settings.onTrailingIconClick != null) {
                                Modifier.clickable(onClick = settings.onTrailingIconClick)
                            } else {
                                Modifier
                            }
                        )
                    )
                }
            },
            singleLine = settings.singleLine,
            enabled = settings.enabled,
            keyboardOptions = settings.keyboardOptions ?: KeyboardOptions.Default,
            visualTransformation = settings.visualTransformation ?: VisualTransformation.None,
            isError = settings.isError,
            supportingText = settings.supportingText?.let { { Text(stringResource(it)) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = modifier.fillMaxWidth()
        )
    }
}