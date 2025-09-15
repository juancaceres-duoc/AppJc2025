package com.example.appjc2025.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appjc2025.model.PlanDia
import com.example.appjc2025.model.RegistroComida
import com.example.appjc2025.ui.theme.AppThemeType
import com.example.appjc2025.utils.Tarjeta

// Función de orden superior
fun operar(valor: Int, op: (Int) -> Int): Int = op(valor)

// Función inline
inline fun evaluarInline(kcal: Int, cond: (Int) -> Boolean): Boolean = cond(kcal)

// Función de extensión sobre Int
fun Int.clasificacion(): String = when {
    this < 1500 -> "Baja"
    this in 1500..2500 -> "Adecuada"
    else -> "Excesiva"
}

// Propiedad de extensión sobre Int
val Int.esExcesivo: Boolean
    get() = this > 3000

// Extensión útil al dominio: kcal promedio de una lista
fun List<RegistroComida>.kcalPromedio(): Int =
    if (isEmpty()) 0 else (sumOf { it.kcal } / size)

@Composable
fun AnalisisMinutaScreen(
    theme: AppThemeType,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    plan: PlanDia
) {
    val todas = plan.comidas.values.flatten()
    val totalKcal = todas.sumOf { it.kcal }
    val doble = operar(totalKcal) { it * 2 }
    val esAdecuado = evaluarInline(totalKcal) { it in 1500..2500 }
    val clasif = totalKcal.clasificacion()
    val excesivo = totalKcal.esExcesivo

    // Ejemplos con colecciones reales
    val livianos = todas.filter { it.kcal < 200 }
    val mensajeEtiqueta = buildString {
        listOf(500, 1000, 2000, 3000).forEach etiqueta@{ ref ->
            if (ref > totalKcal) return@etiqueta
            append("✔ $ref ")
        }
    }

    // Manejo de errores
    val divisionSegura = try {
        totalKcal / (todas.size - todas.size)
    } catch (e: ArithmeticException) {
        "Error: ${e.message}"
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Análisis de minuta",
                theme = theme,
                onToggleTheme = onToggleTheme,
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Resumen",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Total calorías del día: $totalKcal kcal",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text("Ítems registrados: ${todas.size}")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Función de orden superior")
                    Text("Doble del total: $doble")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Función inline")
                    Text("¿Total adecuado (1500–2500)? $esAdecuado")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Lambda con etiqueta")
                    Text("Valores procesados: $mensajeEtiqueta")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Función de extensión (Int.clasificacion)")
                    Text("Clasificación: $clasif")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Propiedad de extensión (Int.esExcesivo)")
                    Text("¿Es excesivo (>3000)? $excesivo")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Filter sobre comidas reales (<200 kcal)")
                    val lista =
                        if (livianos.isEmpty()) "—" else livianos.joinToString { it.descripcion }
                    Text("Livianos: $lista")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Promedio (extensión de lista)")
                    Text("kcal promedio por ítem: ${todas.kcalPromedio()} kcal")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Manejo de errores (try/catch)")
                    Text(divisionSegura.toString())
                }
            }
        }
    }
}
