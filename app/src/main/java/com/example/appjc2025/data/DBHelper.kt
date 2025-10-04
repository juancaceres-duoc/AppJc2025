package com.example.appjc2025.data

import android.content.Context
import android.util.Patterns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

object DBHelper{
    data class ValidationResult(
        val ok: Boolean,
        val errores: List<String> = emptyList()
    )

    data class LoginResult(
        val ok: Boolean,
        val mensaje: String? = null,
        val usuario: Usuario? = null
    )

    fun validarRegistro(context: Context, nombre: String, correo: String, password: String, callback: (ValidationResult) -> Unit){
        val errores = mutableListOf<String>()
        if(nombre.isBlank()) errores += "El campo Nombre es obligatorio."

        if(correo.isBlank()) {
            errores += "El campo Correo es obligatorio."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            errores += "El correo no tiene un formato válido."
        }

        if(password.isBlank()){
            errores += "El campo Contraseña es obligatorio."
        } else if(password.length < 6){
            errores += "La contraseña debe tener al menos 6 caracteres."
        }

        if(errores.isEmpty()){
            val db = AppDB.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                val usuarioExistente = db.usuarioDAO().buscarPorCorreo(correo)
                if (usuarioExistente != null){
                    callback(ValidationResult(false,listOf("El correo ya se encuentra registrado.")))
                }else{
                    callback(ValidationResult(true))
                }
            }

        }else {
            callback(ValidationResult(false, errores))
        }
    }

    fun guardarRegistro(context: Context, nombre: String, correo: String, password: String, callback: (ValidationResult) -> Unit){
        validarRegistro(context,nombre, correo, password){
            validacion ->
            if(!validacion.ok){
                callback(validacion)
                return@validarRegistro
            }

            val usuario = Usuario(nombre = nombre, correo = correo, password = password)
            val db = AppDB.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    db.usuarioDAO().insertar(usuario)
                    withContext(Dispatchers.Main) {
                        callback(ValidationResult(true))
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        callback(ValidationResult(false, listOf("Error BD: ${e.message}")))
                    }
                }
            }
        }
    }

    fun autenticar(context: Context, correo: String, password: String, callback: (LoginResult) -> Unit){
        val correoNorm = correo.trim().lowercase()
        val db = AppDB.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val usuario = db.usuarioDAO().buscarPorCorreo(correoNorm)

            if(usuario == null){
                withContext(Dispatchers.Main){
                    callback(LoginResult(false, "El correo ingresado no existe."))
                }
            }else if(usuario.password != password){
                withContext(Dispatchers.Main){
                    callback(LoginResult(false, "Contraseña incorrecta"))
                }
            } else {
                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit {
                    putBoolean("isLogged", true)
                        .putString("correo", usuario.correo)
                        .putString("nombre", usuario.nombre)
                }

                withContext(Dispatchers.Main) {
                    callback(LoginResult(true, usuario = usuario))
                }
            }
        }
    }
}