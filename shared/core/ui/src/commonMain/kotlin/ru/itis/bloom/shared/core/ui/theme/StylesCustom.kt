package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object StylesCustom {

    val topBarTitle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Start,
    )

    val categoryChipText = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )

    val productCardName = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    val productCardBrand = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )

    val productCardCategory = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    )
}