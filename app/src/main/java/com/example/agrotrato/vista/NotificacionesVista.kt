package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotrato.modelo.Notificacion
import com.example.agrotrato.ui.theme.Amarillo
import com.example.agrotrato.ui.theme.Blanco
import com.example.agrotrato.ui.theme.VerdeClaro
import com.example.agrotrato.ui.theme.VerdeOscuro
import com.example.agrotrato.controlador.NotificacionController


@Composable
fun NotificacionesVista(
    idUsuario: String,
    notificacionController: NotificacionController = NotificacionController(),
    onVolverHome: () -> Unit = {}
) {
    var notificaciones by remember { mutableStateOf<List<Notificacion>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    fun cargarNotificaciones() {
        notificacionController.obtenerNotificacionesFiltradas(
            userId = idUsuario,
            soloLeidas = false,
            onSuccess = { notificaciones = it },
            onError = { mensaje = it }
        )
    }

    LaunchedEffect(true) {
        cargarNotificaciones()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Notificaciones Sin Leer",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeClaro
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = Color.Red)
        }

        if (notificaciones.isNotEmpty()) {
            notificaciones.forEach { notif ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    shape = RoundedCornerShape(16.dp),
                    colors = cardColors(containerColor = VerdeClaro),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = notif.tituloSubasta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Amarillo
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = notif.mensaje,
                            fontSize = 14.sp,
                            color = Blanco
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fecha: ${notif.fecha}",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estado: No leída",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Red
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                notificacionController.marcarComoLeida(
                                    userId = idUsuario,
                                    notificacionId = notif.subastaId,
                                    onSuccess = { cargarNotificaciones() },
                                    onError = { mensaje = it }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VerdeOscuro,
                                contentColor = Blanco
                            )
                        ) {
                            Text("Marcar como leída")
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
