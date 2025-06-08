package com.example.agrotrato.controlador

import com.example.agrotrato.modelo.Notificacion
import com.example.agrotrato.servicio.NotificacionService

class NotificacionController(
    private val service: NotificacionService = NotificacionService()
) {

    fun guardarNotificacion(
        userId: String,
        notificacion: Notificacion,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        service.guardarNotificacion(userId, notificacion, onSuccess, onError)
    }

    fun obtenerNotificaciones(
        userId: String,
        tipo: String? = null,
        onSuccess: (List<Notificacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        service.obtenerNotificaciones(userId, tipo, onSuccess, onError)
    }

    fun eliminarNotificacion(
        userId: String,
        notificacionId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        service.eliminarNotificacion(userId, notificacionId, onSuccess, onError)
    }

    fun marcarComoLeida(
        userId: String,
        notificacionId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        service.marcarComoLeida(userId, notificacionId, onSuccess, onError)
    }

    fun tieneNotificacionesNoLeidas(
        userId: String,
        onResult: (Boolean) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        service.tieneNotificacionesNoLeidas(userId, onResult, onError)
    }

    fun obtenerNotificacionesFiltradas(
        userId: String,
        soloLeidas: Boolean,
        onSuccess: (List<Notificacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        service.obtenerNotificacionesFiltradas(userId, soloLeidas, onSuccess, onError)
    }


    fun generarNotifiaciones(

        idUsuario: String,
        tipoUsuario: String, // "vendedor" o "comprador"
        onNotificacion: (Notificacion) -> Unit,
        onError: (String) -> Unit

    ) {
        service.generarNotificaciones(idUsuario, tipoUsuario, onNotificacion, onError)
    }



}
