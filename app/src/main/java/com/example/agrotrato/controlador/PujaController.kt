package com.example.agrotrato.controlador

import com.example.agrotrato.modelo.Puja
import com.example.agrotrato.servicio.PujaService

class PujaController(
    private val service: PujaService = PujaService()
) {
    fun enviarPuja(
        subastaId: String,
        puja: Puja,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        service.hacerPuja(subastaId, puja, onSuccess, onError)
    }

    fun obtenerMisPujas(
        idUsuario: String,
        onSuccess: (List<Triple<String, String, Puja>>) -> Unit,
        onError: (String) -> Unit
    ) {
        service.obtenerTodasLasPujasConTitulos(
            onSuccess = { pujas ->
                onSuccess(pujas.filter { it.third.corredorId == idUsuario })
            },
            onError = onError
        )
    }

    fun obtenerPrecioActual(
        subastaId: String,
        onSuccess: (Double) -> Unit,
        onError: (String) -> Unit
    ) {
        service.obtenerPrecioActual(subastaId, onSuccess, onError)
    }





    fun eliminarPuja(
        subastaId: String,
        pujaId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        service.eliminarPuja(subastaId, pujaId, onSuccess, onError)
    }



}
