package com.example.agrotrato.servicio

import com.example.agrotrato.modelo.Usuario
import com.example.agrotrato.modelo.UsuarioRegistro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroService {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registrarUsuario(
        registro: UsuarioRegistro,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(registro.email, registro.contrasena)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener onError("Error: UID nulo")

                val usuario = Usuario(
                    id = uid,
                    email = registro.email,
                    nombre = registro.nombre,
                    apellidos = registro.apellidos,
                    nif = registro.nif,
                    clase = registro.clase,
                    telefono = registro.telefono
                )

                db.collection("usuarios")
                    .document(uid)
                    .set(usuario)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError("Error al guardar en Firestore: ${it.message}") }
            }
            .addOnFailureListener {
                onError("Error en el registro: ${it.message}")
            }
    }

    //ACTUALIZAR USUARIO
    fun actualizarUsuario(
        idUsuario: String,
        datos: UsuarioRegistro,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // 1. Actualiza la contraseña
            user.updatePassword(datos.contrasena)
                .addOnSuccessListener {
                    // 2. Luego actualiza el resto de datos en Firestore
                    FirebaseFirestore.getInstance()
                        .collection("usuarios")
                        .document(idUsuario)
                        .update(
                            mapOf(
                                "email" to datos.email,
                                "nombre" to datos.nombre,
                                "apellidos" to datos.apellidos,
                                "nif" to datos.nif,
                                "clase" to datos.clase
                            )
                        )
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onError(e.message ?: "Error al actualizar datos") }
                }
                .addOnFailureListener { e ->
                    onError("Error al cambiar la contraseña: ${e.message}")
                }
        } else {
            onError("Usuario no autenticado")
        }
    }

}
