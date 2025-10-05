package com.example.appjc2025.data

data class LoginResult(
    val ok: Boolean,
    val mensaje: String? = null,
    val correo: String? = null,
    val nombre: String? = null
)

interface AuthRepository {
    fun autenticar(correo: String, password: String): LoginResult
}