package com.example.agrotratosimple


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotratosimple.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.TextFieldDefaults




@Composable
fun Registro(
    onRegistroSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var clase by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Registra tus Datos",
            style = TextStyle(
                color = VerdeOscuro,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        //TextField EMAIL
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
                .padding(bottom = 14.dp)
        )


        //TextField CONTRASENA
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
                .padding(bottom = 14.dp)
        )
        //TextField NOMBRE
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
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
                .padding(bottom = 14.dp)
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
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
                .padding(bottom = 14.dp)
        )
        //TextField NIF
        OutlinedTextField(
            value = nif,
            onValueChange = { nif = it },
            label = { Text("NIF/DNI") },
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
                .padding(bottom = 14.dp)
        )

        //ComboBox CLASE
        ComboBox(
            selectedOption = clase,
            onOptionSelected = { clase = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Button Registro
        Button(
            onClick = {
                val nuevoRegistro =
                    UsuarioRegistro(email, contrasena, nombre, apellidos, nif, clase)
                registrar(
                    nuevoRegistro, onSuccess = {
                        mensajeError = "Registro exitoso"
                        onRegistroSuccess()
                    },
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
            Text("Registrar")
        }

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensajeError,
                color = if (mensajeError == "Registro exitoso") Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        // BOTON PARA SALIR
        Button(
            onClick = {
                onGoToLogin()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeClaro,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Salir", style = TextStyle(fontSize = 16.sp))
        }
    }
}


// Menú desplegable para elegir tipo de usuario
@Composable
fun ComboBox(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text("Elige la clase de Usuario") },
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
            modifier = Modifier.fillMaxWidth()

        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            listOf("VENDEDOR", "COMPRADOR").forEach { option ->
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


//FUNCION PARA REGISTRAR
fun registrar(
    registro: UsuarioRegistro,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (
        registro.email.isNotBlank() &&
        registro.contrasena.isNotBlank() &&
        registro.nombre.isNotBlank() &&
        registro.apellidos.isNotBlank() &&
        registro.nif.isNotBlank() &&
        registro.clase.isNotBlank()
    ) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(registro.email, registro.contrasena)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""
                val usuario = Usuario(
                    id = uid,
                    email = registro.email,
                    nombre = registro.nombre,
                    apellidos = registro.apellidos,
                    nif = registro.nif,
                    clase = registro.clase
                )

                FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(uid)
                    .set(usuario)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError("Error al guardar en Firestore: ${it.message}") }
            }
            .addOnFailureListener {
                onError("Error en el registro: ${it.message}")
            }
    } else {
        onError("Completa todos los campos")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRegistro() {
    Registro(
        onRegistroSuccess = {},
        onGoToLogin = {})
}