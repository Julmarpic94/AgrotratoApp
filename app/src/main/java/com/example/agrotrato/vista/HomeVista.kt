package com.example.agrotrato.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrotrato.ui.theme.*
import com.example.agrotrato.navegacion.Pantalla

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.agrotrato.controlador.NotificacionController
import com.example.agrotrato.controlador.SubastaController


@Composable
fun HomeVista(
    navController: NavController,
    nombre: String,
    tipo: String,
    idUsuario: String,
    onLogout: () -> Unit,
    subastaController: SubastaController = SubastaController(),
    notificacionController: NotificacionController = NotificacionController()
) {
    var mensajeError by remember { mutableStateOf("") }
    var mostrarNotificacionUnica by remember { mutableStateOf(true) }
    var tieneNotificacionNoLeida by remember { mutableStateOf(false) }
    var navegarANotificaciones by remember { mutableStateOf(false) }

    // LANZAMOS EFECTO AL ENTRAR
    LaunchedEffect(true) {
        // 1. Desactivar subastas vencidas
        subastaController.desactivarSubastasFin(
            onSuccess = {
                // 2. Generar notificaciones sobre subastas inactivas y no notificadas
                notificacionController.generarNotifiaciones(
                    idUsuario = idUsuario,
                    tipoUsuario = tipo.lowercase(),
                    onNotificacion = { notificacion ->
                        // 3. Guardar cada notificación en Firestore
                        notificacionController.guardarNotificacion(
                            userId = idUsuario,
                            notificacion = notificacion,
                            onSuccess = { /* Éxito silencioso */ },
                            onError = { mensajeError = it }
                        )
                    },
                    onError = { mensajeError = it }
                )

                // 4. Eliminar subastas inactivas notificadas
                subastaController.eliminarSubastasNotificadas(
                    onSuccess = { /* Limpieza hecha */ },
                    onError = { mensajeError = it }
                )

                // 5. Comprobar si el usuario tiene notificaciones no leídas
                notificacionController.tieneNotificacionesNoLeidas(
                    userId = idUsuario,
                    onResult = { tieneNoLeidas ->
                        if (tieneNoLeidas) {
                            navegarANotificaciones = true
                        }
                    },
                    onError = { mensajeError = it }
                )
            },
            onError = { mensajeError = it }
        )
    }

    // Si hay notificaciones no leídas → redirigir a pantalla
    if (navegarANotificaciones) {
        LaunchedEffect(Unit) {
            navController.navigate(Pantalla.MisNotificaciones.crearRuta(nombre, tipo, idUsuario))
        }
        return
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bienvenido, $nombre 👋",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VerdeClaro)
        )

        Text(
            text = "Rol: $tipo",
            style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic, color = Amarillo)
        )

        LaunchedEffect(idUsuario) {
            notificacionController.tieneNotificacionesNoLeidas(
                userId = idUsuario,
                onResult = { tieneNotificacionNoLeida = it },
                onError = { /* manejo opcional */ }
            )
        }

        if (mostrarNotificacionUnica && tieneNotificacionNoLeida) {
            mostrarNotificacionUnica = false
            navController.navigate(Pantalla.MisNotificaciones.crearRuta(nombre, tipo, idUsuario))
            return
        }


        if (tipo == "VENDEDOR") {
            ActionCard(
                icon = Icons.Default.AddCircle,
                titulo = "Crear subasta",
                descripcion = "Publica un nuevo producto para vender"
            ) {
                navController.navigate(Pantalla.CrearSubasta.crearRuta(idUsuario))
            }
        }

        ActionCard(
            Icons.Default.Search,
            "Explorar Subastas",
            "Consulta las subastas activas"
        ) {
            navController.navigate(Pantalla.VerSubastas.crearRuta(idUsuario, tipo))
        }

        if (tipo == "COMPRADOR") {
            ActionCard(Icons.Default.ShoppingCart, "Mis Pujas", "Gestiona el estado de tus pujas") {
                navController.navigate(Pantalla.MisPujas.crearRuta(idUsuario))
            }
        } else {
            ActionCard(Icons.Default.ShoppingCart, "Mis Subastas", "Gestiona el estado de tus subastas") {
                navController.navigate(Pantalla.MisSubastas.crearRuta(idUsuario))
            }
        }


        ActionCard(Icons.Default.Email, "Notificaciones", "Revisa tus notificaciones") {
            navController.navigate(Pantalla.NotificacionesLeidas.crearRuta(idUsuario))
        }


        ActionCard(Icons.Default.AccountCircle, "Perfil", "Gestiona tu perfil") {
            navController.navigate(Pantalla.MisPerfil.crearRuta(idUsuario))
        }

        ActionCard(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión", "Salir de tu cuenta") {
            onLogout()
        }

        // Mostrar errores si hay
        if (mensajeError.isNotEmpty()) {
            Text(text = mensajeError, color = Color.Red)
        }
    }
}

@Composable
fun ActionCard(
    icon: ImageVector,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = VerdeClaro),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Amarillo,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Amarillo
                )
                Text(text = descripcion, fontSize = 14.sp, color = Blanco)
            }
        }
    }
}
