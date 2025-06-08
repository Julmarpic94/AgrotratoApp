package com.example.agrotrato.controlador

import com.example.agrotrato.modelo.UsuarioRegistro
import com.example.agrotrato.servicio.RegistroService

class RegistroController(
    private val registroService: RegistroService = RegistroService()
) {
    //Registrar usuario
    fun registrar(
        registro: UsuarioRegistro,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (
            registro.email.isNotBlank() &&
            registro.contrasena.isNotBlank() &&
            registro.nombre.isNotBlank() &&
            registro.apellidos.isNotBlank() &&
            registro.nif.isNotBlank() &&
            registro.clase.isNotBlank()&&
            registro.telefono.isNotBlank()
        ) {
            registroService.registrarUsuario(registro, onSuccess, onError)
        } else {
            onError("Completa todos los campos")
        }
    }

    //Actulizar Perfil
    fun actualizarUsuario(
        idUsuario: String,
        datos: UsuarioRegistro,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        registroService.actualizarUsuario(idUsuario, datos, onSuccess, onError)
    }


}
