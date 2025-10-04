package com.example.appjc2025.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDAO {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}