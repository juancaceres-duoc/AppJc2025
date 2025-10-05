package com.example.appjc2025.data

class LoginInteractor(private val repo: AuthRepository) {

    fun login(correo: String, password: String): LoginResult {
        val u = correo.trim()
        val p = password

        if (u.isEmpty() || p.isEmpty()) {
            return LoginResult(ok = false, mensaje = "Campos vacíos")
        }

        return repo.autenticar(u, p)
    }
}