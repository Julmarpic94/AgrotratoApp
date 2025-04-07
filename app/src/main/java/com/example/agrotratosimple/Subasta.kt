package com.example.agrotratosimple


data class Oferta(
    val corredorId: String = "",
    val monto: Double = 0.0,
    val fecha: String = ""
)

data class Subasta(
    val id: String,
    val producto: String,
    val descripcion: String,
    val precioInicial: Double,
    val agricultorId: String,
    val fechaLimite: String,
    val estado: String,
    val ofertas: List<Oferta> = emptyList()
)
