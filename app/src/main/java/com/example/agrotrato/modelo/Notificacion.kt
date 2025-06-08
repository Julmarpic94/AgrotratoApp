package com.example.agrotrato.modelo

data class Notificacion(
    val subastaId: String = "",
    val tituloSubasta: String = "",
    val mensaje: String = "",
    val fecha: String = "",
    val tipo: String = "",
    val telefono: String = "",
    val leida: Boolean = false
)
