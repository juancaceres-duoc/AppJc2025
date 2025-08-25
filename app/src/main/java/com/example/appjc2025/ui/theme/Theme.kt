package com.example.appjc2025.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color

enum class AppThemeType { Normal, Accessible }

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val NormalColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF111111),
    onSurface = Color(0xFF111111)
)

private val AccessibleColors = darkColorScheme(
    primary = Color(0xFFFFD600),
    secondary = Color(0xFF00E5FF),
    background = Color.Black,
    surface = Color(0xFF121212),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun AppJc2025Theme(
    theme: AppThemeType = AppThemeType.Normal,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppThemeType.Normal -> NormalColors
        AppThemeType.Accessible -> AccessibleColors
    }

    val typography = when (theme) {
        AppThemeType.Normal -> NormalTypography
        AppThemeType.Accessible -> AccessibleTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
