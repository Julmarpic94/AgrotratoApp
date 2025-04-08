package com.example.agrotratosimple


import android.net.Uri

sealed class Pantalla(val ruta: String) {

    object Login : Pantalla("login")
    object Registro : Pantalla("registro")

    // Ruta con argumentos: nombre y tipo
    object Home : Pantalla("home/{nombre}/{tipo}") {
        // Función para construir la ruta con los valores reales
        fun crearRuta(nombre: String, tipo: String): String {
            return "home/${Uri.encode(nombre)}/$tipo"
        }
    }
}
