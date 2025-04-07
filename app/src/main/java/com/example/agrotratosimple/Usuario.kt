package com.example.agrotratosimple

data class UsuarioRegistro(
    val email: String,
    val contrasena: String,
    val nombre: String,
    val apellidos: String,
    val nif: String,
    val clase: String
)


data class Usuario (
    val id: String,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val nif: String,
    val clase: String
)


