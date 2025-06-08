package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.agrotrato.modelo.Notificacion
import com.example.agrotrato.ui.theme.*
import com.example.agrotrato.controlador.NotificacionController

@Composable
fun NotificacionesLeidas(
    idUsuario: String,
    notificacionController: NotificacionController = NotificacionController(),
    onVolverHome: () -> Unit = {}
) {
    var notificaciones by remember { mutableStateOf<List<Notificacion>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    fun cargar() {
        notificacionController.obtenerNotificacionesFiltradas(
            userId = idUsuario,
            soloLeidas = true,
            onSuccess = { notificaciones = it },
            onError = { mensaje = it }
        )
    }

    LaunchedEffect(true) { cargar() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Historial de Notificaciones",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeClaro
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (mensaje.isNotEmpty()) Text(mensaje, color = Color.Red)

        if (notificaciones.isNotEmpty()) {
        notificaciones.forEach { notif ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeClaro)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(notif.tituloSubasta, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Amarillo)
                    Text(notif.mensaje, fontSize = 14.sp, color = Blanco)
                    Text("Fecha: ${notif.fecha}", fontSize = 12.sp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            notificacionController.eliminarNotificacion(
                                userId = idUsuario,
                                notificacionId = notif.subastaId,
                                onSuccess = { cargar() },
                                onError = { mensaje = it }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Blanco)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
        } else if (mensaje.isEmpty()) {
            Text("No tienes notificaciones aún.", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onVolverHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClaro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Volver", style = TextStyle(fontSize = 16.sp))
        }
    }
}
