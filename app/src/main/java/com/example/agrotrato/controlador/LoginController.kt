package com.example.agrotrato.controlador

import com.example.agrotrato.servicio.AuthService

class LoginController(
    private val authService: AuthService = AuthService()
) {
    fun login(
        email: String,
        contrasena: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        authService.autenticar(email, contrasena, onSuccess, onError)
    }
}

