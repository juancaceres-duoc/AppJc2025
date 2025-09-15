package com.example.appjc2025.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.appjc2025.model.PlanDia
import com.example.appjc2025.model.RegistroComida
import com.example.appjc2025.model.TipoComida
import com.example.appjc2025.ui.theme.AppThemeType
import com.example.appjc2025.ui.theme.Purple40
import com.example.appjc2025.utils.FechaHelper
import com.example.appjc2025.utils.Tarjeta

private data class EditState(val tipo: TipoComida, val original: RegistroComida?)

@Composable
fun HomeScreen(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    onIrAnalisis: (PlanDia) -> Unit
) {
    val fechaTexto = remember { FechaHelper.fechaTexto(FechaHelper.hoy()) }
    var plan by remember { mutableStateOf(PlanDia()) }
    var edit by rememberSaveable { mutableStateOf<EditState?>(null) }
    var mostrarDialogoMetas by remember { mutableStateOf(false) }

    val kcalTotales by remember(plan) {
        derivedStateOf { plan.comidas.values.flatten().sumOf { it.kcal } }
    }
    val avance by remember(plan, kcalTotales) {
        derivedStateOf { (kcalTotales.toFloat() / plan.metaKcal).coerceIn(0f, 1f) }
    }
    val tip by remember(plan, kcalTotales) {
        derivedStateOf {
            when {
                kcalTotales == 0 -> "Aún sin comidas."
                kcalTotales < plan.metaKcal * 0.6 -> "Vas bien. Faltan ~${plan.metaKcal - kcalTotales} kcal."
                kcalTotales > plan.metaKcal * 1.1 -> "Hoy estás sobre la meta. Ajusta tu consumo diario."
                else -> "Dentro de rango."
            }
        }
    }

    fun agregarOEditarComida(tipo: TipoComida, reg: RegistroComida) {
        val prev = plan.comidas[tipo].orEmpty()
        val nueva = if (prev.any { it.id == reg.id }) {
            prev.map { if (it.id == reg.id) reg else it }
        } else {
            prev + reg
        }
        plan = plan.copy(comidas = plan.comidas + (tipo to nueva))
    }

    fun quitarComida(tipo: TipoComida, id: String) {
        val nueva = plan.comidas[tipo].orEmpty().filterNot { it.id == id }
        plan = plan.copy(comidas = plan.comidas + (tipo to nueva))
    }

    fun cambiarAgua(delta: Int) {
        plan = plan.copy(agua = (plan.agua + delta).coerceIn(0, plan.metaAgua))
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Minuta diaria",
                theme = theme,
                onToggleTheme = onToggleTheme,
                showBack = true,
                onBack = onBack
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    fechaTexto,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                Tarjeta(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Resumen del día",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.weight(1f))
                            val coloresTextoPrimario = ButtonDefaults.textButtonColors(
                                contentColor = Purple40,
                                disabledContentColor = Purple40.copy(alpha = 0.38f)
                            )
                            TextButton(onClick = { mostrarDialogoMetas = true }, colors = coloresTextoPrimario) {
                                Text("Metas")
                            }
                        }

                        Text(
                            "Kcal totales: $kcalTotales / ${plan.metaKcal}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        LinearProgressIndicator(
                            progress = { avance },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            tip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Meta de agua: ${plan.metaAgua} vasos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(TipoComida.entries.toTypedArray()) { tipo ->
                SeccionComidaSimple(
                    tipo = tipo,
                    items = plan.comidas[tipo].orEmpty(),
                    onAgregar = { edit = EditState(tipo, null) },
                    onEditar = { reg -> edit = EditState(tipo, reg) },
                    onQuitar = { reg -> quitarComida(tipo, reg.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Tarjeta(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Agua: ${plan.agua} / ${plan.metaAgua} vasos",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.width(12.dp))
                        OutlinedButton(onClick = { cambiarAgua(-1) }) { Text("–") }
                        Spacer(Modifier.width(8.dp))
                        OutlinedButton(onClick = { cambiarAgua(+1) }) { Text("+") }
                    }
                }
            }

            item {
                Button(
                    onClick = { onIrAnalisis(plan) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Analizar Minuta", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    edit?.let { e ->
        DialogoAgregarEditarComida(
            descripcionInicial = e.original?.descripcion ?: "",
            kcalInicial = e.original?.kcal?.toString() ?: "",
            onCerrar = { edit = null },
            onConfirmar = { descripcion, kcal ->
                val id = e.original?.id ?: "${e.tipo.name}_${System.currentTimeMillis()}"
                agregarOEditarComida(
                    e.tipo,
                    RegistroComida(id = id, descripcion = descripcion, kcal = kcal)
                )
                edit = null
            }
        )
    }

    if (mostrarDialogoMetas) {
        DialogoMetas(
            metaAguaInicial = plan.metaAgua,
            metaKcalInicial = plan.metaKcal,
            onCerrar = { mostrarDialogoMetas = false },
            onConfirmar = { nuevaAgua, nuevaKcal ->
                plan = plan.copy(metaAgua = nuevaAgua, metaKcal = nuevaKcal)
                mostrarDialogoMetas = false
            }
        )
    }
}

@Composable
private fun SeccionComidaSimple(
    tipo: TipoComida,
    items: List<RegistroComida>,
    onAgregar: () -> Unit,
    onEditar: (RegistroComida) -> Unit,
    onQuitar: (RegistroComida) -> Unit,
    modifier: Modifier = Modifier
) {
    Tarjeta(modifier) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    tipo.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onAgregar) { Text("Añadir") }
            }
            if (items.isEmpty()) {
                Text(
                    "Sin registro.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                items.forEach { reg ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(reg.descripcion, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "${reg.kcal} kcal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = { onEditar(reg) }) { Text("Editar") }
                        TextButton(onClick = { onQuitar(reg) }) { Text("Quitar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogoAgregarEditarComida(
    descripcionInicial: String,
    kcalInicial: String,
    onCerrar: () -> Unit,
    onConfirmar: (descripcion: String, kcal: Int) -> Unit
) {
    var descripcion by rememberSaveable { mutableStateOf(descripcionInicial) }
    var kcalTexto by rememberSaveable { mutableStateOf(kcalInicial) }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (descripcionInicial.isEmpty()) "Añadir elemento" else "Editar elemento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = kcalTexto,
                    onValueChange = { kcalTexto = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Calorías (kcal)") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val coloresTextoPrimario = ButtonDefaults.textButtonColors(
                        contentColor = Purple40,
                        disabledContentColor = Purple40.copy(alpha = 0.38f)
                    )
                    TextButton(onClick = onCerrar, colors = coloresTextoPrimario) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            val kcal = kcalTexto.toIntOrNull()
                            when {
                                descripcion.isBlank() -> error = "Ingresa una descripción."
                                kcal == null || kcal <= 0 || kcal > 2500 ->
                                    error = "Ingresa calorías válidas (1–2500)."
                                else -> onConfirmar(descripcion.trim(), kcal)
                            }
                        },
                        colors = coloresTextoPrimario
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogoMetas(
    metaAguaInicial: Int,
    metaKcalInicial: Int,
    onCerrar: () -> Unit,
    onConfirmar: (metaAgua: Int, metaKcal: Int) -> Unit
) {
    var aguaTexto by rememberSaveable { mutableStateOf(metaAguaInicial.toString()) }
    var kcalTexto by rememberSaveable { mutableStateOf(metaKcalInicial.toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Configurar metas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF111111))

                OutlinedTextField(
                    value = aguaTexto,
                    onValueChange = { aguaTexto = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Meta de agua (vasos)") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = kcalTexto,
                    onValueChange = { kcalTexto = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Meta de calorías (kcal)") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    val coloresTextoPrimario = ButtonDefaults.textButtonColors(
                        contentColor = Purple40,
                        disabledContentColor = Purple40.copy(alpha = 0.38f)
                    )
                    TextButton(onClick = onCerrar, colors = coloresTextoPrimario) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            val agua = aguaTexto.toIntOrNull()
                            val kcal = kcalTexto.toIntOrNull()
                            when {
                                agua == null || agua !in 1..20 -> error = "Agua: ingresa un valor entre 1 y 20."
                                kcal == null || kcal !in 800..5000 -> error = "Calorías: ingresa entre 800 y 5000."
                                else -> onConfirmar(agua, kcal)
                            }
                        },
                        colors = coloresTextoPrimario
                    ) { Text("Guardar") }
                }
            }
        }
    }
}
