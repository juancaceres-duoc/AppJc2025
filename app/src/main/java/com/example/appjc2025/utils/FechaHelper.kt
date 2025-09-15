package com.example.appjc2025.utils

import java.text.SimpleDateFormat
import java.util.*

object FechaHelper {
    private fun isoFormatter() = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    private fun largoFormatter(locale: Locale) = SimpleDateFormat("EEEE d 'de' MMMM", locale)

    private val localeEsCl = Locale("es", "CL")

    fun hoy(): String = isoFormatter().format(Date())

    fun fechaTexto(fechaIso: String, locale: Locale = localeEsCl): String {
        val date = try {
            isoFormatter().parse(fechaIso)
        } catch (_: Exception) {
            Date()
        }
        return largoFormatter(locale)
            .format(date)
            .replaceFirstChar { it.titlecase(locale) }
    }
}