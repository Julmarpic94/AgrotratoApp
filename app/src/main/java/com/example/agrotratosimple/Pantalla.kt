package com.example.agrotratosimple


sealed class Pantalla(val ruta: String) {
    object Login : Pantalla("login")
    object Registro : Pantalla("registro")
    object Home : Pantalla("home")
}
