package com.example.agrotrato.navegacion

import android.net.Uri

sealed class Pantalla(val ruta: String) {

    object Login : Pantalla("login")
    object Registro : Pantalla("registro")
    object CrearSubasta : Pantalla("crearSubasta/{idUsuario}"){
        fun crearRuta(idUsuario: String): String = "crearSubasta/$idUsuario"
    }

    object VerSubastas : Pantalla("verSubastas/{idUsuario}"){
        fun crearRuta(idUsuario: String): String = "verSubastas/$idUsuario"
    }

    object MisSubastas : Pantalla("misSubastas/{idUsuario}"){
        fun crearRuta(idUsuario: String): String = "misSubastas/$idUsuario"
    }

    object MisPujas : Pantalla("misPujas/{idUsuario}"){
        fun crearRuta(idUsuario: String): String = "misPujas/$idUsuario"
    }

    object MisPerfil : Pantalla("miPerfil/{idUsuario}"){
        fun crearRuta(idUsuario: String): String = "miPerfil/$idUsuario"
    }


    object HacerPuja : Pantalla("hacerPuja/{subastaId}/{idUsuario}") {
        fun crearRuta(subastaId: String, idUsuario: String): String =
            "hacerPuja/$subastaId/$idUsuario"
    }


    object Home : Pantalla("home/{nombre}/{tipo}/{uid}") {
        fun crearRuta(nombre: String, tipo: String, uid: String): String {
            return "home/${Uri.encode(nombre)}/${Uri.encode(tipo)}/$uid"
        }
    }
}
