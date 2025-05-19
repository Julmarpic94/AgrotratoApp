package com.example.agrotrato.servicio

import com.example.agrotrato.modelo.Puja
import com.google.firebase.firestore.FirebaseFirestore

class PujaService {
    private val db = FirebaseFirestore.getInstance()

    fun hacerPuja(
        subastaId: String,
        puja: Puja,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val subastaRef = db.collection("subastas").document(subastaId)

        subastaRef.get()
            .addOnSuccessListener { doc ->
                val precioActual = doc.getDouble("precioActual") ?: 0.0

                if (puja.monto > precioActual) {
                    // 1. Guardar puja en la subcolección
                    subastaRef.collection("pujas")
                        .add(puja)
                        .addOnSuccessListener {
                            // 2. Actualizar el precioActual en el documento
                            subastaRef.update("precioActual", puja.monto)
                                .addOnSuccessListener { onSuccess() }
                                .addOnFailureListener { e -> onError("Puja guardada pero error al actualizar precio: ${e.message}") }
                        }
                        .addOnFailureListener { e -> onError("Error al guardar la puja: ${e.message}") }
                } else {
                    onError("Tu puja debe ser mayor al precio actual de ${precioActual}€")
                }
            }
            .addOnFailureListener { e ->
                onError("Error al obtener subasta: ${e.message}")
            }
    }

    fun obtenerTodasLasPujasConTitulos(
        onSuccess: (List<Triple<String, String, Puja>>) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val subastasRef = db.collection("subastas")

        subastasRef.get()
            .addOnSuccessListener { subastas ->
                val resultado = mutableListOf<Triple<String, String, Puja>>()
                var subastasProcesadas = 0

                if (subastas.isEmpty) {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                }

                for (subastaDoc in subastas) {
                    val subastaId = subastaDoc.id
                    val titulo = subastaDoc.getString("titulo") ?: "Sin título"

                    subastaDoc.reference.collection("pujas").get()
                        .addOnSuccessListener { pujasSnapshot ->
                            for (pujaDoc in pujasSnapshot) {
                                val puja = pujaDoc.toObject(Puja::class.java).copy(id = pujaDoc.id)
                                resultado.add(Triple(titulo, subastaId, puja))
                            }

                            subastasProcesadas++
                            if (subastasProcesadas == subastas.size()) {
                                onSuccess(resultado)
                            }
                        }
                        .addOnFailureListener {
                            subastasProcesadas++
                            if (subastasProcesadas == subastas.size()) {
                                onSuccess(resultado) // aún si algunas fallaron
                            }
                        }
                }
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al obtener las subastas")
            }
    }



    fun eliminarPuja(
        subastaId: String,
        pujaId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val subastaRef = db.collection("subastas").document(subastaId)

        // 1. Eliminar puja
        subastaRef.collection("pujas").document(pujaId)
            .delete()
            .addOnSuccessListener {
                // 2. Obtener pujas restantes
                subastaRef.collection("pujas").get()
                    .addOnSuccessListener { pujasSnapshot ->
                        // 3. Obtener la mayor entre las pujas que quedan
                        val mayorPuja = pujasSnapshot.documents
                            .mapNotNull { it.getDouble("monto") }
                            .maxOrNull()

                        // 4. Obtener el precioInicial para que no baje de ahí
                        subastaRef.get()
                            .addOnSuccessListener { subastaDoc ->
                                val precioInicial = subastaDoc.getDouble("precioInicial") ?: 0.0
                                val nuevoPrecioActual = maxOf(precioInicial, mayorPuja ?: 0.0)

                                // 5. Actualizar el precioActual con la última puja más alta
                                subastaRef.update("precioActual", nuevoPrecioActual)
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { e ->
                                        onError("Puja eliminada pero error al actualizar precio: ${e.message}")
                                    }
                            }
                            .addOnFailureListener { e ->
                                onError("Puja eliminada pero error al obtener subasta: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        onError("Puja eliminada pero error al obtener pujas restantes: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onError("Error al eliminar puja: ${e.message}")
            }
    }


    fun obtenerPrecioActual(
        subastaId: String,
        onSuccess: (Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("subastas").document(subastaId).get()
            .addOnSuccessListener { doc ->
                val precio = doc.getDouble("precioActual")
                if (precio != null) {
                    onSuccess(precio)
                } else {
                    onError("Precio no disponible")
                }
            }
            .addOnFailureListener { e ->
                onError("Error al cargar precio: ${e.message}")
            }
    }



}
