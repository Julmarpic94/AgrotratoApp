package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotrato.controlador.RegistroController
import com.example.agrotrato.modelo.UsuarioRegistro
import com.example.agrotrato.ui.theme.Amarillo
import com.example.agrotrato.ui.theme.Blanco
import com.example.agrotrato.ui.theme.VerdeClaro
import com.example.agrotrato.ui.theme.VerdeOscuro


@Composable
fun PerfilVista(
    idUsuario: String,
    onVolverHome: () -> Unit,
    controller: RegistroController = RegistroController()
) {

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Actuliza tus Datos",
            style = TextStyle(
                color = VerdeOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Campos
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            singleLine = true,
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = nif,
            onValueChange = { nif = it },
            label = { Text("NIF/DNI") },
            singleLine = true,
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevoValor ->
                if (nuevoValor.all { it.isDigit() }) {
                    telefono = nuevoValor
                }
            },
            label = { Text("Teléfono") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = customTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        ComboBox(
            selectedOption = clase,
            onOptionSelected = { clase = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Actualizar
        Button(
            onClick = {
                val datosActualizados = UsuarioRegistro(email, contrasena, nombre, apellidos, nif, clase, telefono)

                controller.actualizarUsuario(
                    idUsuario = idUsuario,
                    datos = datosActualizados,
                    onSuccess = {
                        mensajeError = "Perfil actualizado correctamente"
                    },
                    onError = { error -> mensajeError = error }
                )

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amarillo,
                contentColor = Blanco),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Actualizar")
        }


        Spacer(modifier = Modifier.height(14.dp))

        // Botón Salir
        Button(
            onClick = onVolverHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClaro,
                contentColor = Blanco),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Volver", style = TextStyle(fontSize = 16.sp))
        }
    }
}

