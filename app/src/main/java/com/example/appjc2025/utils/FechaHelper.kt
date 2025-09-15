package com.example.appjc2025.utils

import java.text.SimpleDateFormat
import java.util.*

object FechaHelper {
    private fun isoFormatter() = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    private fun largoFormatter(locale: Locale) = SimpleDateFormat("EEEE d 'de' MMMM", locale)

    private val localeEsCl = Locale("es", "CL")

    fun hoy(): String = isoFormatter().format(Date())

    fun fechaTexto(fechaIso: String, locale: Locale = localeEsCl): String {
        val date = runCatching { isoFormatter().parse(fechaIso) }.getOrNull() ?: Date()
        val txt = largoFormatter(locale).format(date)
        return txt.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(locale) else ch.toString()
        }
    }
}