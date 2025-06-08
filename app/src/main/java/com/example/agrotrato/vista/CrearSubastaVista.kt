package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotrato.modelo.Subasta
import com.example.agrotrato.ui.theme.*
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.app.DatePickerDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.platform.LocalContext
import com.example.agrotrato.controlador.SubastaController
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun CrearSubastaVista(
    idUsuario: String,
    onVolverHome: () -> Unit,
    controller: SubastaController = SubastaController()
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cantidadKg by remember { mutableStateOf("") }
    var tipoProducto by remember { mutableStateOf("") }
    var precioInicial by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Crea un nueva Subasta",
            style = TextStyle(
                color = VerdeOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )
        //TITULO SUBASTA
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Titulo Subasta") },
            singleLine = true,
            colors = customTextFieldColors(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        )
        //DESCRIPCION SUBASTA
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion Subasta") },
            colors = customTextFieldColors(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        )
        //COMBO PARA ELEGIR PRODUCTO
        ComboBoxProducto(
            selectedOption = tipoProducto,
            onOptionSelected = { tipoProducto = it }
        )
        //CANTIDA DE KILO
        OutlinedTextField(
            value = cantidadKg,
            onValueChange = { cantidadKg = it },
            label = { Text("Cantidad (Kg)") },
            colors = customTextFieldColors(),
            //Solo permitimos el teclado númerico, omitiendo legras
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        )
        //PRECIO DEL KILO
        OutlinedTextField(
            value = precioInicial,
            onValueChange = { precioInicial = it },
            label = { Text("Precio por Kg (€)") },
            colors = customTextFieldColors(),
            //Solo permitimos el teclado númerico, omitiendo legras
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        )
        //Date picker para elegir fecha inicio
        OutlinedTextField(
            value = fechaInicio,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha Inicial") },
            colors = customTextFieldColors(),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            fechaInicio = formatter.format(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Elegir fecha")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
        )

        // DATE PICKER para FECHA FIN
        OutlinedTextField(
            value = fechaFin,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha Final") },
            colors = customTextFieldColors(),
            trailingIcon = {
                IconButton(onClick = {
                    val picker = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            fechaFin = formatter.format(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    picker.datePicker.minDate = System.currentTimeMillis() // 💡 No permite fechas pasadas
                    picker.show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Elegir fecha")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )

        Button(
            onClick = {
                if (
                    titulo.isBlank() || descripcion.isBlank() ||
                    tipoProducto.isBlank() || precioInicial.isBlank() ||
                    cantidadKg.isBlank() || fechaInicio.isBlank() || fechaFin.isBlank()
                ) {
                    mensaje = "Completa todos los campos"
                    return@Button
                }

                // Validamos fechas parseando a fecha para que no pueda ser el inicio despues del fin
                try {
                    val fechaInicioDate = formatter.parse(fechaInicio)
                    val fechaFinDate = formatter.parse(fechaFin)

                    if (fechaInicioDate != null && fechaFinDate != null && !fechaFinDate.after(fechaInicioDate)) {
                        mensaje = "La fecha final debe ser posterior a la fecha de inicio"
                        return@Button
                    }
                } catch (e: Exception) {
                    return@Button
                }

                val nueva = Subasta(
                    id = UUID.randomUUID().toString(),
                    titulo = titulo,
                    descripcion = descripcion,
                    cantidadKg = cantidadKg.toInt(),
                    precioInicial = precioInicial.toDouble(),
                    precioActual = precioInicial.toDouble(),
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                    tipoProducto = tipoProducto,
                    vendedorId = idUsuario,
                    activo = true,
                    notificada = false
                )

                controller.crearSubasta(
                    nueva,
                    onSuccess = {
                        mensaje = "Subasta creada con éxito"
                    },
                    onError = { errorMsg ->
                        mensaje = errorMsg
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amarillo,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(0.5f).height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Crear", style = TextStyle(fontSize = 16.sp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                onVolverHome()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClaro,
                contentColor = Color.White
            ),

            modifier = Modifier.fillMaxWidth(0.5f).height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Volver", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = if (mensaje.contains("éxito")) Color.Green else Color.Red
            )
        }
    }
}

// Combobox para elegir el producto
@Composable
fun ComboBoxProducto(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text("Elige el tipo de producto") },
            trailingIcon = {
                IconButton(onClick = { expandido = !expandido }) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.arrow_down_float),
                        contentDescription = null,
                        tint = if (expandido) Color.Green else Color.Gray
                    )
                }
            },
            readOnly = true,
            colors = customTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            listOf("ALMENDRA", "NUEZ", "PISTACHO").forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expandido = false
                    }
                )
            }
        }
    }
}

