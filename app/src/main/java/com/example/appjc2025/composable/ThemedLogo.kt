package com.example.appjc2025.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.appjc2025.R
import com.example.appjc2025.ui.theme.AppThemeType

@Composable
fun ThemedLogo(theme: AppThemeType, modifier: Modifier = Modifier) {
    val logoRes = if (theme == AppThemeType.Accessible) {
        R.drawable.logo_nutri_contraste
    } else {
        R.drawable.logo_nutri_normal
    }

    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Logo NutriApp",
        modifier = modifier.fillMaxWidth(0.5f),
        contentScale = ContentScale.Fit
    )
}