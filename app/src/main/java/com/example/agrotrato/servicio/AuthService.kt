package com.example.agrotrato.servicio

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthService {
    fun autenticar(
        email: String,
        contrasena: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isNotEmpty() && contrasena.isNotEmpty()) {
            Log.d("AuthService", "Intentando autenticar con email: $email")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, contrasena)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid
                    if (uid == null) {
                        Log.e("AuthService", "UID es null después de login")
                        onError("Error: UID no encontrado tras autenticación.")
                        return@addOnSuccessListener
                    }

                    Log.d("AuthService", "Login exitoso, UID: $uid")
                    val db = FirebaseFirestore.getInstance()

                    db.collection("usuarios")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val nombre = document.getString("nombre") ?: "Usuario"
                                val tipo = document.getString("clase") ?: "COMPRADOR"
                                Log.d("AuthService", "Documento Firestore recuperado: $nombre, $tipo")
                                onSuccess(nombre, tipo, uid)
                            } else {
                                Log.e("AuthService", "Documento no existe para UID: $uid")
                                onError("No se encontraron datos de usuario.")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("AuthService", "Fallo al recuperar documento Firestore", e)
                            onError("Error al recuperar datos del usuario: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("AuthService", "Fallo al iniciar sesión", e)
                    onError(e.message ?: "Error al iniciar sesión.")
                }
        } else {
            Log.w("AuthService", "Campos vacíos en login")
            onError("Campos vacíos")
        }
    }
}
