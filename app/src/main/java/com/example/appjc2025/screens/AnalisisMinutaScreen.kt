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

// Función de extensión
fun Int.clasificacion(): String = when {
    this < 1500 -> "Baja"
    this in 1500..2500 -> "Adecuada"
    else -> "Excesiva"
}

// Propiedad de extensión
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

    //Uso de función de orden superior
    val diferenciaVsMeta = operar(totalKcal) { it - plan.metaKcal }

    //Uso de función Inline
    val dentroDeRango = evaluarInline(totalKcal) {
        it in (plan.metaKcal - 200)..(plan.metaKcal + 200)
    }

    //Extensión
    val clasif = totalKcal.clasificacion()
    val excesivo = totalKcal.esExcesivo


    // Filter
    val livianos = todas.filter { it.kcal < 200 }
    val potentes = todas.filter { it.kcal >= 600 }

    //Lambda con etiqueta
    val hitos = listOf(0.25f, 0.5f, 0.75f, 1.0f)
    val progresoHitos = buildString {
        hitos.forEach etiqueta@{ h ->
            val paso = (plan.metaKcal * h).toInt()
            if (totalKcal < paso) return@etiqueta
            append("✔ ${paso}kcal ")
        }
    }.ifBlank { "—" }

    // Manejo de errores
    val promedioSeguro = try {
        totalKcal / todas.size
    } catch (e: ArithmeticException) {
        0
    }

    val mayor = todas.maxByOrNull { it.kcal }
    val menor = todas.minByOrNull { it.kcal }
    val hidratacionOk = evaluarInline(plan.agua) { it >= plan.metaAgua }

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
                    Text("Total del día: $totalKcal kcal", style = MaterialTheme.typography.bodyLarge)
                    Text("Ítems registrados: ${todas.size}")
                    Text("Meta kcal: ${plan.metaKcal} | Meta agua: ${plan.metaAgua} vasos")
                    Text("Agua bebida: ${plan.agua} vasos (${if (hidratacionOk) "cumplida" else "pendiente"})")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Orden superior (operar)")
                    val msg = when {
                        diferenciaVsMeta > 0 -> "Sobre la meta por +${diferenciaVsMeta} kcal"
                        diferenciaVsMeta < 0 -> "Bajo la meta por ${-diferenciaVsMeta} kcal"
                        else -> "Exactamente en la meta"
                    }
                    Text(msg)
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Función inline (evaluarInline)")
                    Text("¿Dentro de ±200 kcal de la meta? $dentroDeRango")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Lambda con etiqueta (hitos hacia la meta)")
                    Text(progresoHitos)
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Extensión (Int.clasificacion)")
                    Text("Clasificación fija: $clasif")
                }
            }
            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Propiedad de extensión (Int.esExcesivo)")
                    Text("¿Excesivo (>3000 kcal)? $excesivo")
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Comidas livianas (<200 kcal)")
                    val lista = if (livianos.isEmpty()) "—" else livianos.joinToString { it.descripcion }
                    Text(lista)
                }
            }
            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Comidas muy calóricas (≥600 kcal)")
                    val lista = if (potentes.isEmpty()) "—" else potentes.joinToString { it.descripcion }
                    Text(lista)
                }
            }

            Tarjeta(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Promedio y extremos")
                    Text("Promedio seguro: $promedioSeguro kcal/ítem")
                    Text("Mayor: ${mayor?.let { "${it.descripcion} (${it.kcal} kcal)" } ?: "—"}")
                    Text("Menor: ${menor?.let { "${it.descripcion} (${it.kcal} kcal)" } ?: "—"}")
                }
            }
        }
    }
}