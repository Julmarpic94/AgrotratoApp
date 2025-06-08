package com.example.agrotrato.servicio

import com.example.agrotrato.modelo.Notificacion
import com.example.agrotrato.modelo.Puja
import com.example.agrotrato.modelo.Subasta
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificacionService {

    private val db = FirebaseFirestore.getInstance()

    //ALMACENAR NOTIFICACIONES
    fun guardarNotificacion(
        userId: String,
        notificacion: Notificacion,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        // Usamos un ID único (puede ser UUID, o el ID de la subasta si es único por usuario)
        val docId = notificacion.subastaId // o UUID.randomUUID().toString() si prefieres

        db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .document(docId)
            .set(notificacion)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al guardar la notificación") }
    }


    //OBTENER NOTIFICACIONES
    fun obtenerNotificaciones(
        userId: String,
        tipo: String? = null, // "ganador", "vendedor" o null para todas
        onSuccess: (List<Notificacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        val ref = db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")

        val query = if (tipo != null) {
            ref.whereEqualTo("tipo", tipo)
        } else {
            ref
        }

        query.get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Notificacion::class.java)?.copy(subastaId = doc.id)
                }
                onSuccess(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al obtener notificaciones")
            }
    }


    //ELIMINAR NOTIFICACION
    fun eliminarNotificacion(
        userId: String,
        notificacionId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .document(notificacionId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al eliminar notificación") }
    }


    //MARCAR NOTIFICACION COMO LEIDA
    fun marcarComoLeida(
        userId: String,
        notificacionId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .document(notificacionId)
            .update("leida", true)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al marcar como leída") }
    }



    fun tieneNotificacionesNoLeidas(
        userId: String,
        onResult: (Boolean) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .whereEqualTo("leida", false)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.documents.isNotEmpty())
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al verificar notificaciones")
            }
    }

    fun obtenerNotificacionesFiltradas(
        userId: String,
        soloLeidas: Boolean,
        onSuccess: (List<Notificacion>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .whereEqualTo("leida", soloLeidas)
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Notificacion::class.java)?.copy(subastaId = doc.id)
                }
                onSuccess(lista)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al filtrar notificaciones")
            }
    }


    // METODO PARA GENERAR NOTIFICACIONES (basado en subastas inactivas)
    fun generarNotificaciones(
        idUsuario: String,
        tipoUsuario: String, // "vendedor" o "comprador"
        onNotificacion: (Notificacion) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("subastas")
            .whereEqualTo("activo", false) // Buscamos subastas inactivas
            .get()
            .addOnSuccessListener { subastas ->
                subastas.documents.forEach { doc ->
                    val subasta = doc.toObject(Subasta::class.java)
                    if (subasta != null && !subasta.notificada) {
                        val subastaRef = db.collection("subastas").document(subasta.id)

                        subastaRef.collection("pujas").get()
                            .addOnSuccessListener { pujasSnapshot ->
                                val pujas = pujasSnapshot.documents.mapNotNull {
                                    it.toObject(Puja::class.java)
                                }

                                val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                                val pujaMayor = pujas.maxByOrNull { it.monto }

                                if (pujaMayor != null) {
                                    // Obtener teléfonos
                                    db.collection("usuarios").document(pujaMayor.corredorId).get()
                                        .addOnSuccessListener { docComprador ->
                                            val telComprador = docComprador.getString("telefono") ?: "no disponible"

                                            db.collection("usuarios").document(subasta.vendedorId).get()
                                                .addOnSuccessListener { docVendedor ->
                                                    val telVendedor = docVendedor.getString("telefono") ?: "no disponible"

                                                    val notiVendedor = Notificacion(
                                                        subastaId = subasta.id,
                                                        tituloSubasta = subasta.titulo,
                                                        mensaje = "Tu subasta ha finalizado. Se ha vendido por un precio de ${pujaMayor.monto}€. Contacta con el comprador a traves de este Teléfono: $telComprador",
                                                        fecha = fecha,
                                                        telefono = telComprador,
                                                        tipo = "vendedor"
                                                    )

                                                    val notiGanador = Notificacion(
                                                        subastaId = subasta.id,
                                                        tituloSubasta = subasta.titulo,
                                                        mensaje = "¡Has ganado la subasta con tu puja de ${pujaMayor.monto}€! Contacta con el vendedor a través de este teléfono: $telVendedor",
                                                        fecha = fecha,
                                                        telefono = telVendedor,
                                                        tipo = "ganador"
                                                    )

                                                    if (idUsuario == subasta.vendedorId && tipoUsuario == "vendedor") {
                                                        onNotificacion(notiVendedor)
                                                    }
                                                    if (idUsuario == pujaMayor.corredorId && tipoUsuario == "comprador") {
                                                        onNotificacion(notiGanador)
                                                    }

                                                    // Marcar subasta como notificada
                                                    subastaRef.update("notificada", true)
                                                }
                                                .addOnFailureListener { e ->
                                                    onError("Error obteniendo teléfono vendedor: ${e.message}")
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            onError("Error obteniendo teléfono comprador: ${e.message}")
                                        }
                                } else {
                                    // Sin pujas
                                    val notiSinPujas = Notificacion(
                                        subastaId = subasta.id,
                                        tituloSubasta = subasta.titulo,
                                        mensaje = "Tu subasta ha finalizado pero no ha recibido ninguna puja.",
                                        fecha = fecha,
                                        tipo = "vendedor"
                                    )

                                    if (idUsuario == subasta.vendedorId && tipoUsuario == "vendedor") {
                                        onNotificacion(notiSinPujas)
                                    }

                                    subastaRef.update("notificada", true)
                                }
                            }
                            .addOnFailureListener { e ->
                                onError("Error al obtener pujas: ${e.message}")
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                onError("Error al revisar subastas: ${e.message}")
            }
    }




}


