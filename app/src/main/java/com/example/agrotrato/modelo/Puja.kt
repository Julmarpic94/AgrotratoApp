package com.example.agrotrato.modelo

data class Puja(
    val id: String = "", // <--- Nuevo campo necesario
    val corredorId: String = "",
    val monto: Double = 0.0,
    val fecha: String = ""
)


