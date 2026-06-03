package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color

data class ProductCardSettings(
    val productName: String,
    val brand: String? = null,
    val category: String,
    val imageUrl: String? = null,
    val onClick: () -> Unit = {},
    val placeholderColor: Color? = null
)