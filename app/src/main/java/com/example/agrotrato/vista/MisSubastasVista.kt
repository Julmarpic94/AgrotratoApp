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
import com.example.agrotrato.controlador.SubastaController
import com.example.agrotrato.modelo.Subasta
import com.example.agrotrato.ui.theme.*

@Composable
fun MisSubastasVista(
    idUsuario: String,
    onVolverHome: () -> Unit,
    controller: SubastaController = SubastaController()
) {
    var listaSubastas by remember { mutableStateOf<List<Subasta>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }


    //LANZAMOS UNA CORUTINA PARA FILTRAR LAS SUBASTAS POR ID DEL VENDEDOR
    LaunchedEffect(true) {
        controller.obtenerSubastas(
            onSuccess = { todasSubastas ->
                listaSubastas = todasSubastas.filter { it.vendedorId == idUsuario }
            },
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
            text = "Mis Subastas",
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

        //COLUMNA VISUALIZADORA DE CARDS
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listaSubastas) { subasta ->
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
                            " ${subasta.titulo} ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Amarillo
                        )
                        Text("Producto: ${subasta.tipoProducto}", color = Color.White)
                        Text("Descripción: ${subasta.descripcion}", color = Color.White)
                        Text("Cantidad: ${subasta.cantidadKg} Kg", color = Color.White)
                        Text("Precio Inicial: ${subasta.precioInicial} €", color = Color.White)
                        Text("Precio Actual: ${subasta.precioActual} €", color = Color.White)
                        Text("Inicio: ${subasta.fechaInicio}", color = Color.White)
                        Text("Fin: ${subasta.fechaFin}", color = Color.White)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                controller.eliminarSubasta(
                                    subasta.id,
                                    onSuccess = {
                                        // Actualizar la lista local tras eliminar
                                        listaSubastas = listaSubastas.filter { it.id != subasta.id }
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
                            Text("Eliminar Subasta")
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
