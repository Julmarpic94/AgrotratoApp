package com.example.agrotrato.servicio

import com.example.agrotrato.modelo.Subasta
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SubastaService {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("subastas")

    //CREAR SUBASTA
    fun crearSubasta(
        subasta: Subasta,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        coleccion.document(subasta.id)
            .set(subasta)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(error.message ?: "Error al crear subasta") }
    }

    //OBTENER TODAS LAS SUBASTAS
    fun obtenerSubastas(
        onSuccess: (List<Subasta>) -> Unit,
        onError: (String) -> Unit
    ) {
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        coleccion.get()
            .addOnSuccessListener { result ->
                val listaSubastas = result.documents.mapNotNull { doc ->
                    val subasta = doc.toObject(Subasta::class.java)
                    // Comprobamos si la subasta ya venció
                    if (subasta != null && subasta.fechaFin == hoy && subasta.activo) {
                        // Desactivamos
                        coleccion.document(subasta.id).update("activo", false)
                    }
                    subasta
                }.filter { it.activo } // solo mostramos activas
                onSuccess(listaSubastas)
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al obtener subastas")
            }
    }

    //OBTENER TODAS LA SUBASTA ACTIVAS
    fun obtenerSubastasActivas(
        onSuccess: (List<Subasta>) -> Unit,
        onError: (String) -> Unit
    ) {
        coleccion
            .whereEqualTo("activo", true)
            .get()
            .addOnSuccessListener { result ->
                val listaSubastas = result.documents.mapNotNull { it.toObject(Subasta::class.java) }
                onSuccess(listaSubastas)
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al obtener subastas")
            }
    }

    //Eliminar suabasata
    fun eliminarSubasta(
        subastaId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("subastas").document(subastaId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError("Error al eliminar subasta: ${e.message}") }
    }


    //DESACTIVAR SUSBASTAS ACABADAS
    fun desactivarSubastasFin(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val db = FirebaseFirestore.getInstance()

        db.collection("subastas")
            .whereEqualTo("activo", true)
            .get()
            .addOnSuccessListener { subastas ->
                val tareas = subastas.documents.mapNotNull { doc ->
                    val subasta = doc.toObject(Subasta::class.java)
                    if (subasta != null && subasta.fechaFin == hoy) {
                        db.collection("subastas").document(subasta.id).update("activo", false)
                    } else null
                }

                // Esperar a que todas las actualizaciones terminen
                Tasks.whenAllComplete(tareas)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onError("Error al desactivar subastas: ${e.message}") }
            }
            .addOnFailureListener { e ->
                onError("Error al obtener subastas: ${e.message}")
            }
    }

    fun marcarSubastaNotificada(
        subastaId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("subastas")
            .document(subastaId)
            .update("notificada", true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al marcar subasta como notificada") }
    }

    fun eliminarSubastasNotificadas(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("subastas")
            .whereEqualTo("activo", false)
            .whereEqualTo("notificada", true)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onError(e.message ?: "Error al eliminar subastas notificadas") }
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error al obtener subastas notificadas") }
    }



}






