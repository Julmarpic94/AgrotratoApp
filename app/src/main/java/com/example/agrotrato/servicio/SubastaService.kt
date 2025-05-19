package com.example.agrotrato.servicio

import com.example.agrotrato.modelo.Subasta
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

    fun revisarSubastasFinalizadas(
        vendedorId: String,
        onNotificaciones: (List<Pair<Subasta, Puja?>>) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val ahora = System.currentTimeMillis()
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        db.collection("subastas")
            .whereEqualTo("vendedorId", vendedorId)
            .whereEqualTo("activo", true)
            .get()
            .addOnSuccessListener { subastasSnapshot ->
                val notificaciones = mutableListOf<Pair<Subasta, Puja?>>()

                val tareasPendientes = subastasSnapshot.documents.size
                var procesadas = 0

                for (doc in subastasSnapshot) {
                    val subasta = doc.toObject(Subasta::class.java)?.copy(id = doc.id) ?: continue
                    val fechaFin = formato.parse(subasta.fechaFin)?.time ?: continue

                    if (ahora > fechaFin) {
                        // Subasta finalizada
                        doc.reference.update("activo", false)

                        // Buscar la puja más alta
                        doc.reference.collection("pujas")
                            .get()
                            .addOnSuccessListener { pujasSnapshot ->
                                val pujaGanadora = pujasSnapshot
                                    .mapNotNull { it.toObject(Puja::class.java) }
                                    .maxByOrNull { it.monto }

                                notificaciones.add(Pair(subasta, pujaGanadora))
                                procesadas++
                                if (procesadas == tareasPendientes) {
                                    onNotificaciones(notificaciones)
                                }
                            }
                            .addOnFailureListener {
                                notificaciones.add(Pair(subasta, null))
                                procesadas++
                                if (procesadas == tareasPendientes) {
                                    onNotificaciones(notificaciones)
                                }
                            }
                    } else {
                        procesadas++
                        if (procesadas == tareasPendientes) {
                            onNotificaciones(notificaciones)
                        }
                    }
                }

                if (tareasPendientes == 0) onNotificaciones(emptyList())
            }
            .addOnFailureListener {
                onError("Error al revisar subastas: ${it.message}")
            }
    }




}
