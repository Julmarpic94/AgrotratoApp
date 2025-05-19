package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotrato.controlador.PujaController
import com.example.agrotrato.modelo.Puja
import com.example.agrotrato.ui.theme.*

@Composable
fun MisPujasVista(
    idUsuario: String,
    onVolverHome: () -> Unit,
    controller: PujaController = PujaController()
) {
    var listaPujas by remember { mutableStateOf<List<Triple<String, String, Puja>>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        controller.obtenerMisPujas(
            idUsuario,
            onSuccess = { listaPujas = it },
            onError = { mensaje = it }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Pujas",
            style = TextStyle(
                color = VerdeOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = Color.Red, modifier = Modifier.padding(bottom = 12.dp))
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listaPujas) { (tituloSubasta, subastaId, puja) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = VerdeClaro),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Subasta: $tituloSubasta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Amarillo
                        )
                        Text("Monto pujado: ${puja.monto} €", color = Color.White)
                        Text("Fecha: ${puja.fecha}", color = Color.White)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                controller.eliminarPuja(
                                    subastaId = subastaId,
                                    pujaId = puja.id,
                                    onSuccess = {
                                        listaPujas = listaPujas.filterNot { it.third.id == puja.id }
                                    },
                                    onError = { mensaje = it }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Eliminar Puja")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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