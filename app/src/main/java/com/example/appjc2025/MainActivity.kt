package com.example.appjc2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.appjc2025.ui.theme.AppJc2025Theme
import androidx.compose.ui.tooling.preview.Preview
import com.example.appjc2025.composable.AppNav
import com.example.appjc2025.utils.RegisterHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppScaffold() {
    AppJc2025Theme {
        AppNav()
    }
}