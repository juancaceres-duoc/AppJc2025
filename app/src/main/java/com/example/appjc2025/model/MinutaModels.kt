package com.example.appjc2025.model

enum class TipoComida(val titulo: String) {
    DESAYUNO("Desayuno"),
    ALMUERZO("Almuerzo"),
    CENA("Cena")
}

data class RegistroComida(
    val id: String,
    val descripcion: String,
    val kcal: Int
)

data class PlanDia(
    val comidas: Map<TipoComida, List<RegistroComida>> = mapOf(
        TipoComida.DESAYUNO to emptyList(),
        TipoComida.ALMUERZO to emptyList(),
        TipoComida.CENA to emptyList()
    ),
    val agua: Int = 0,
    val metaAgua: Int = 8,
    val metaKcal: Int = 2000
)
