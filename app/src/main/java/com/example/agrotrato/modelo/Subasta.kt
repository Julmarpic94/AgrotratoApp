package com.example.agrotrato.modelo

data class Subasta(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val cantidadKg: Int = 0,
    val precioActual: Double = 0.0, // Para mantener el precio actual de la subasta
    val precioInicial: Double = 0.0,
    val fechaInicio: String = "",
    val fechaFin: String = "",
    val tipoProducto: String = "",
    val vendedorId: String = "",
    val activo: Boolean = true // activa por defecto
)

