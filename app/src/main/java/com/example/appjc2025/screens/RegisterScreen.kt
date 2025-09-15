package com.example.appjc2025.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appjc2025.ui.theme.AppThemeType
import com.example.appjc2025.utils.RegisterHelper
import com.example.appjc2025.utils.RegisterHelper.registros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Registro",
                showBack = true,
                theme = theme,
                onToggleTheme = onToggleTheme,
                onBack = onBack
            )
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text("Crear cuenta", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Ocultar" else "Mostrar")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // Botones
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            RegisterHelper.guardarRegistro(nombre, correo, password)
                            Toast.makeText(
                                context,
                                "Datos guardados correctamente. Total registros: ${registros.size}",
                                Toast.LENGTH_LONG
                            ).show()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrarme")
                    }

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}