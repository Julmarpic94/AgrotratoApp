package com.example.agrotrato.vista

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import java.util.*

@Composable
fun HacerPujaVista(
    subastaId: String,
    idUsuario: String,
    onPujaExitosa: () -> Unit,
    onVolver: () -> Unit,
    controller: PujaController = PujaController()
) {
    var monto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var precioActual by remember { mutableStateOf<Double?>(null) }

    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaActual = formatter.format(Calendar.getInstance().time)

    LaunchedEffect(true) {
        controller.obtenerPrecioActual(
            subastaId,
            onSuccess = { precioActual = it },
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
            text = "Realizar una Puja",
            style = TextStyle(
                color = VerdeOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (precioActual != null) {
            Text(
                text = "Precio actual: $precioActual €",
                style = TextStyle(
                    color = VerdeOscuro,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }


        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Introduce tu puja en €") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        Button(
            onClick = {
                val nuevaPuja = Puja(
                    corredorId = idUsuario,
                    monto = monto.toDouble(),
                    fecha = fechaActual
                )

                controller.enviarPuja(
                    subastaId = subastaId,
                    puja = nuevaPuja,
                    onSuccess = {
                        mensaje = "Puja realizada con éxito"
                        onPujaExitosa()
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
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Pujar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolver,
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

        Spacer(modifier = Modifier.height(12.dp))
        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = if (mensaje.contains("éxito")) Color.Green else Color.Red
            )
        }
    }
}
