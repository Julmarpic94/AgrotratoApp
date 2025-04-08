package com.example.agrotratosimple

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotratosimple.ui.theme.Amarillo
import com.example.agrotratosimple.ui.theme.VerdeClaro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun Login(
    onLoginSuccess: (String, String) -> Unit,
    onGoToRegistro: () -> Unit,
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
            text = "\"Subasta tu Cosecha\"",
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
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = VerdeClaro,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = VerdeClaro,
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = VerdeClaro,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedLabelColor = VerdeClaro,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = VerdeClaro,
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = VerdeClaro,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        //BOTON PARA LOGEARSE
        Button(
            onClick = {
                autentificar(
                    email,
                    contrasena,
                    onSuccess = { nombre, tipo -> onLoginSuccess(nombre, tipo) },
                    onError = { error -> mensajeError = error }
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

        //BOTON PARA REGISTRARSE
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

//FUNCION PARA AUTENTIFICAR CON FIREBASE
fun autentificar(
    usuario: String,
    contrasena: String,
    onSuccess: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario, contrasena)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                // Consultamos Firestore para obtener los datos del usuario
                FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        val nombre = document.getString("nombre") ?: "Usuario"
                        val tipo = document.getString("clase") ?: "COMPRADOR"

                        onSuccess(nombre, tipo)
                    }
                    .addOnFailureListener {
                        onError("Error al recuperar datos del usuario.")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Error al iniciar sesión.")
            }
    }
}



