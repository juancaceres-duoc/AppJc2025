package com.example.appjc2025.utils

object RegisterHelper {
    val registros = mutableListOf<Map<String, String>>()
    data class LoginResult(
        val ok: Boolean,
        val mensaje: String? = null,
        val usuario: Map<String, String>? = null
    )

    fun guardarRegistro(nombre: String, correo: String, password: String) {
        val registro = mapOf(
            "nombre" to nombre,
            "correo" to correo,
            "password" to password
        )
        registros.add(registro)
    }

    fun autenticar(correo: String, password: String): LoginResult{
        val correoNorm = correo.trim().lowercase()

        val user = registros.firstOrNull {it["correo"]?.trim()?.lowercase() == correoNorm}
            ?: return LoginResult(false, "Correo Incorrecto")

        return if (user["password"] == password) {
            LoginResult(true, usuario = user)
        } else {
            LoginResult(false, "Credenciales Inválidas")
        }
    }
}

