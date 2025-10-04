package com.example.appjc2025.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appjc2025.composable.ThemedLogo
import com.example.appjc2025.data.DBHelper
import com.example.appjc2025.ui.theme.AppThemeType
import com.example.appjc2025.utils.RegisterHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginApp(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "NutriApp",
                showBack = false,
                theme = theme,
                onToggleTheme = onToggleTheme
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Incrementar")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo
                ThemedLogo(theme = theme)

                // Campo Usuario
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón Login
                Button(
                    onClick = {
                        val u = correo.trim()
                        val p = password
                        if (u.isEmpty() || p.isEmpty()){
                            Toast.makeText(context, "Ingresa tu correo y contraseña", Toast.LENGTH_LONG).show()
                            return@Button
                        }else {
                            DBHelper.autenticar(context,u, p){
                                res ->
                                if (res.ok) {
                                    onLoginSuccess()
                                } else {
                                    val msg = res.mensaje?.trim().takeUnless { it.isNullOrEmpty() }
                                        ?: "Credenciales inválidas"
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión")
                }

                // Otros sitios
                TextButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
                    Text("Crear cuenta")
                }
                TextButton(onClick = onGoForgot, modifier = Modifier.fillMaxWidth()) {
                    Text("¿Olvidaste tu contraseña?")
                }
            }
        }
    }
}