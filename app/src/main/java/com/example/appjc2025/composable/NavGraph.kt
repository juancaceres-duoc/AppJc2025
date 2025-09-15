package com.example.appjc2025.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appjc2025.Routes
import com.example.appjc2025.screens.*
import com.example.appjc2025.ui.theme.AppJc2025Theme
import com.example.appjc2025.ui.theme.AppThemeType
import com.example.appjc2025.utils.MinutaHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    var theme by remember { mutableStateOf(AppThemeType.Normal) }
    val toggleTheme = {
        theme = if (theme == AppThemeType.Normal) AppThemeType.Accessible else AppThemeType.Normal
    }

    AppJc2025Theme(theme = theme) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ) {
            composable(Routes.LOGIN) {
                LoginApp(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onGoRegister = { navController.navigate(Routes.REGISTER) },
                    onGoForgot = { navController.navigate(Routes.FORGOT) },
                    onLoginSuccess = { navController.navigate(Routes.HOME) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.FORGOT) {
                ForgotPasswordScreen(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onBack = { navController.popBackStack() },
                    onIrAnalisis = { plan: PlanDia ->
                        MinutaHelper.plan = plan
                        navController.navigate(Routes.MINUTA)
                    }
                )
            }

            composable(Routes.MINUTA) {
                val plan = MinutaHelper.plan ?: PlanDia()
                AnalisisMinutaScreen(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onBack = { navController.popBackStack() },
                    plan = plan
                )
            }
        }
    }
}