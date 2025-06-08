package com.example.agrotrato.controlador

import com.example.agrotrato.modelo.Subasta
import com.example.agrotrato.servicio.SubastaService

class SubastaController(
    private val servicio: SubastaService = SubastaService()
) {
    fun crearSubasta(
        subasta: Subasta,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        servicio.crearSubasta(subasta, onSuccess, onError)
    }

    fun obtenerSubastas(
        onSuccess: (List<Subasta>) -> Unit,
        onError: (String) -> Unit
    ) {
        servicio.obtenerSubastas(onSuccess, onError)
    }

    fun obtenerSubastasActivas(
        onSuccess: (List<Subasta>) -> Unit,
        onError: (String) -> Unit
    ) {
        servicio.obtenerSubastasActivas(onSuccess, onError)
    }

    fun eliminarSubasta(
        subastaId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        servicio.eliminarSubasta(subastaId, onSuccess, onError)
    }

    fun desactivarSubastasFin(
    onSuccess: () -> Unit,
    onError: (String) -> Unit
    ){
        servicio.desactivarSubastasFin(onSuccess, onError)
    }

    fun marcarSubastaNotificada(
        subastaId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        servicio.marcarSubastaNotificada(subastaId, onSuccess, onError)
    }


    fun eliminarSubastasNotificadas(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        servicio.eliminarSubastasNotificadas(onSuccess, onError)
    }





}
