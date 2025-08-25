package com.example.appjc2025.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NormalTypography = Typography()

val AccessibleTypography = Typography(
    bodyLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.SansSerif
    ),
    titleLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = FontFamily.SansSerif
    ),
    labelLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif
    )
)