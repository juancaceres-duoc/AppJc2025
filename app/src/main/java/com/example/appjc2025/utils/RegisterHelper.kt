package com.example.appjc2025.utils

object RegisterHelper {
    val registros = mutableListOf<Map<String, String>>()

    fun guardarRegistro(nombre: String, correo: String, password: String) {
        val registro = mapOf(
            "nombre" to nombre,
            "correo" to correo,
            "password" to password
        )
        registros.add(registro)
    }
}

