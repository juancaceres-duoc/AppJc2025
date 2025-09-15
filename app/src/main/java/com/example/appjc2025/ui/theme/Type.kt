package com.example.appjc2025.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NormalTypography = Typography()

val AccessibleTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold
    ),
    titleSmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.SansSerif
    ),
    bodyMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif
    ),
    bodySmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    ),
    labelLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    ),
    labelSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif
    )
)