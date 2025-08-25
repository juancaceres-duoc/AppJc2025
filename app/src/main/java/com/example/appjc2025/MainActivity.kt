package com.example.appjc2025

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import com.example.appjc2025.ui.theme.AppJc2025Theme
import com.example.appjc2025.ui.theme.AppThemeType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav()
{
    val navController = rememberNavController()
    var theme by remember { mutableStateOf(AppThemeType.Normal) }
    val toggleTheme = { theme = if (theme == AppThemeType.Normal) AppThemeType.Accessible else AppThemeType.Normal }

    AppJc2025Theme(theme = theme) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ){
            composable(Routes.LOGIN){
                LoginApp(
                    theme = theme,
                    onToggleTheme = toggleTheme,
                    onGoRegister = { navController.navigate(Routes.REGISTER) },
                    onGoForgot   = { navController.navigate(Routes.FORGOT) }
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    title: String,
    showBack: Boolean,
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onBack: (() -> Unit)? = null,
    onMenu: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBack?.invoke() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            } else {
                IconButton(onClick = { onMenu?.invoke() }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menú")
                }
            }
        },
        actions = {
            TextButton(
                onClick = onToggleTheme,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (theme == AppThemeType.Normal) "Modo Accesibilidad" else "Modo Normal")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginApp(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                onClick = { /* TODO: ACCIONES */ },
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
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
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
                    onClick = { /* TODO: LOGIN */},
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
                            guardarRegistro(nombre, correo, password)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit
){
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Recuperar contraseña",
                theme = theme,
                onToggleTheme = onToggleTheme,
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )


            Button(onClick = {
                Toast.makeText(
                    context,
                    "Correo enviado a: $email",
                    Toast.LENGTH_LONG
                ).show()
                onBack()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Enviar instrucciones")
            }
        }
    }
}

val registros = mutableListOf<Map<String, String>>()

fun guardarRegistro(nombre: String, correo: String, password: String) {
    val registro = mapOf(
        "nombre" to nombre,
        "correo" to correo,
        "password" to password
    )
    registros.add(registro)
}

@Preview(showBackground = true)
@Composable
fun PreviewAppScaffold() {
    AppJc2025Theme {
        AppNav()
    }
}