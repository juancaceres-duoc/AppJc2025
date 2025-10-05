package com.example.appjc2025

import com.example.appjc2025.data.AuthRepository
import com.example.appjc2025.data.LoginInteractor
import com.example.appjc2025.data.LoginResult

import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*


class LoginUnitTest {

    private val repo = mock<AuthRepository>()
    private val interactor = LoginInteractor(repo)

    @Test
    fun campos_vacios() {
        val r = interactor.login("", "")
        assertFalse(r.ok)
        assertEquals("Campos vacíos", r.mensaje)
        verify(repo, never()).autenticar(any(), any())
    }

    @Test
    fun correo_no_existe() {
        whenever(repo.autenticar("noexiste@mail.com", "123456"))
            .thenReturn(LoginResult(ok = false, mensaje = "Correo no existe"))

        val r = interactor.login("noexiste@mail.com", "123456")

        assertFalse(r.ok)
        assertEquals("Correo no existe", r.mensaje)
        verify(repo).autenticar("noexiste@mail.com", "123456")
    }

    @Test
    fun password_incorrecta() {
        whenever(repo.autenticar("miguel@mail.com", "wrong"))
            .thenReturn(LoginResult(ok = false, mensaje = "Contraseña incorrecta"))

        val r = interactor.login("miguel@mail.com", "wrong")

        assertFalse(r.ok)
        assertEquals("Contraseña incorrecta", r.mensaje)
        verify(repo).autenticar("miguel@mail.com", "wrong")
    }

    @Test
    fun login_correcto() {
        whenever(repo.autenticar("miguel@mail.com", "1234"))
            .thenReturn(LoginResult(ok = true, correo = "miguel@mail.com", nombre = "Miguel"))

        val r = interactor.login("miguel@mail.com", "1234")

        assertTrue(r.ok)
        assertEquals("miguel@mail.com", r.correo)
        assertEquals("Miguel", r.nombre)
        verify(repo).autenticar("miguel@mail.com", "1234")
    }
}