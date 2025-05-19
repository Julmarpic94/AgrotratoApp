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
import androidx.navigation.NavController
import com.example.agrotrato.controlador.SubastaController
import com.example.agrotrato.modelo.Subasta
import com.example.agrotrato.ui.theme.*
import com.example.agrotrato.navegacion.Pantalla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerSubastasVista(
    navController: NavController,
    idUsuario: String,
    onVolverHome: () -> Unit,
    controller: SubastaController = SubastaController()
) {
    var listaSubastas by remember { mutableStateOf<List<Subasta>>(emptyList()) }
    var mensaje by remember { mutableStateOf("") }

    var filtroTipoProducto by remember { mutableStateOf("Todos") }
    var filtroPrecioMaximo by remember { mutableStateOf("") }
    var filtroCantidadMinima by remember { mutableStateOf("") }

    val tiposProducto = listOf("Todos", "PISTACHO", "NUEZ", "ALMENDRA")
    var expanded by remember { mutableStateOf(false) }

    // Cargar subastas
    LaunchedEffect(true) {
        controller.obtenerSubastasActivas(
            onSuccess = { listaSubastas = it },
            onError = { mensaje = it }
        )
    }

    // Aplicar filtros
    val subastasFiltradas = listaSubastas.filter { subasta ->
        (filtroTipoProducto == "Todos" || subasta.tipoProducto.equals(filtroTipoProducto, ignoreCase = true)) &&
                (filtroPrecioMaximo.isBlank() || subasta.precioInicial <= filtroPrecioMaximo.toDoubleOrNull() ?: Double.MAX_VALUE) &&
                (filtroCantidadMinima.isBlank() || subasta.cantidadKg >= filtroCantidadMinima.toIntOrNull() ?: 0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Subastas Disponibles",
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

        // Filtros
        Column(modifier = Modifier.fillMaxWidth()) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = filtroTipoProducto,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de producto") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = customTextFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tiposProducto.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                filtroTipoProducto = tipo
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = filtroPrecioMaximo,
                onValueChange = { filtroPrecioMaximo = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Precio máximo (€)") },
                colors = customTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = filtroCantidadMinima,
                onValueChange = { filtroCantidadMinima = it.filter { c -> c.isDigit() } },
                label = { Text("Cantidad mínima (kg)") },
                colors = customTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            //Boton Limpiar filtros
            Button(
                onClick = {
                    filtroTipoProducto = "Todos"
                    filtroPrecioMaximo = ""
                    filtroCantidadMinima = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amarillo,
                    contentColor = Blanco),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Limpiar filtros")
            }

        }

        // Lista de subastas Filtradas
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(subastasFiltradas) { subasta ->
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
                        Text("Precio Actual: ${subasta.precioActual} €", color = Color.White)
                        Text("Inicio: ${subasta.fechaInicio}", color = Color.White)
                        Text("Fin: ${subasta.fechaFin}", color = Color.White)

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate(
                                        Pantalla.HacerPuja.crearRuta(subasta.id, idUsuario)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Amarillo,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Hacer Puja")
                            }
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
