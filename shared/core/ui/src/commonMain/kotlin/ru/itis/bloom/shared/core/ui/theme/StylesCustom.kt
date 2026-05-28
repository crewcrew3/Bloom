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

    val bottomBarLabel = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
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

    //collaps text section
    val sectionTitle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    val sectionContent = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    )

    //product detail
    val productDetailTitle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )

    val productDetailSubtitle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val productDetailLabel = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )

    val productDetailValue = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
}