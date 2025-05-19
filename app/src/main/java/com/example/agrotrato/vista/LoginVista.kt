package com.example.agrotrato.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotrato.controlador.LoginController
import com.example.agrotrato.ui.theme.Amarillo
import com.example.agrotrato.ui.theme.VerdeClaro
import com.example.agrotrato.R


@Composable
fun LoginVista(
    onLoginSuccess: (String, String, String) -> Unit,
    onGoToRegistro: () -> Unit,
    controller: LoginController = LoginController()
) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Login Image",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Subasta tu Cosecha",
            style = TextStyle(
                color = VerdeClaro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            colors = textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                controller.login(
                    email,
                    contrasena,
                    onSuccess = { nombre, tipo, uid  -> onLoginSuccess(nombre, tipo, uid) },
                    onError = { mensajeError = it }
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
            Text(text = "Entrar", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onGoToRegistro() },
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClaro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Registro", style = TextStyle(fontSize = 16.sp))
        }

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeError,
                color = if (mensajeError == "Bienvenido") Color.Green else Color.Red,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedLabelColor = VerdeClaro,
    unfocusedLabelColor = Color.Gray,
    focusedIndicatorColor = VerdeClaro,
    unfocusedIndicatorColor = Color.LightGray,
    cursorColor = VerdeClaro,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
