package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.StringResource

data class FormFieldSettings(
    val label: StringResource,
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: StringResource? = null,
    val isError: Boolean = false,
    val supportingText: StringResource? = null,
    val trailingIcon: Painter? = null,
    val onTrailingIconClick: (() -> Unit)? = null,
    val singleLine: Boolean = true,
    val enabled: Boolean = true,
    val keyboardOptions: KeyboardOptions? = null,
    val visualTransformation: androidx.compose.ui.text.input.VisualTransformation? = null
)