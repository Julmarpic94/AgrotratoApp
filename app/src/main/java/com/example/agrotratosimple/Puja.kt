package com.example.agrotratosimple

data class Puja(
    val id: String,
    val titulo: String,
    val precioInicial: Double,
    val agricultorId: String,
    val fechaLimite: String,
    val ofertas: List<Oferta> = emptyList()
)
