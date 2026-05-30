package ru.itis.bloom.shared.core.ui.utils

import androidx.compose.runtime.Composable

interface ImagePickerCallback {
    fun onImageSelected(uri: String?)
}

@Composable
expect fun rememberImagePicker(
    callback: ImagePickerCallback
): () -> Unit