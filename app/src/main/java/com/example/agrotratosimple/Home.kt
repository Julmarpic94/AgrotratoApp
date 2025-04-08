package com.example.agrotratosimple

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agrotratosimple.ui.theme.Amarillo
import com.example.agrotratosimple.ui.theme.Blanco
import com.example.agrotratosimple.ui.theme.VerdeClaro


@Composable
fun HomePantalla(
    nombre: String,
    tipo: String, // o "VENDEDOR"
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Saludo principal y muestra de tipo de cuenta
        Text(
            text = "Bienvenido, $nombre 👋",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VerdeClaro)
        )

        Text(
            text = "Rol: $tipo",
            style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic, color = Amarillo)
        )

        // CREAR SUBASTA
        // Card: Crear Subasta (solo si es vendedor)
        if (tipo == "VENDEDOR") {
            ActionCard(
                icon = Icons.Default.AddCircle,
                titulo = "Crear subasta",
                descripcion = "Publica un nuevo producto para vender"
            ) {
                // HACER CREAR subasta
            }
        }


        //Explorar SUBASTAS DISPONIBLES             AMBOS
        ActionCard(
            icon = Icons.Default.Search,
            titulo = "Explorar Subastas",
            descripcion = "Consulta las subastas activas"
        ) {
            // HACER ACCION PARA MOSTRAR SUSBASTAS
        }

        //SUBASTAS O PUJAS
        //Ver Pujas
        if (tipo == "COMPRADOR") {
            ActionCard(
                icon = Icons.Default.ShoppingCart,
                titulo = "Mis Pujas",
                descripcion = "Gestiona el estado de tus pujas"
            ) {
                // HACER VISOR DE SUBASTAS
            }
        }
        //Ver mis Subastas
        if (tipo == "VENDEDOR") {
            ActionCard(
                icon = Icons.Default.ShoppingCart,
                titulo = "Mis Subastas",
                descripcion = "Gestiona el estado de tus subastas"
            ) {
                // HACER VISOR DE SUBASTAS
            }
        }

        //HISTORIALES
        //Historial de subastas
        if (tipo == "VENDEDOR") {
            ActionCard(
                icon = Icons.Default.Home,
                titulo = "Historial de Subastas",
                descripcion = "Revisas tu historial de subastas"
            ) {
                // HCER CREAR subasta
            }
        }
        //Historial de Pujas
        if (tipo == "COMPRADOR") {
            ActionCard(
                icon = Icons.Default.Home,
                titulo = "Historial de Pujas",
                descripcion = "Revisa tu historial de pujas"
            ) {
                // HCER CREAR subasta
            }
        }

        //CARD PARA GESTIONAR PERFIL
        ActionCard(
            icon = Icons.Default.AccountCircle,
            titulo = "Perfil",
            descripcion = "Gestiona tu perfil"
        ) {
            // HACER GESTOR DE PERFIL
        }

        //OPCION CERRAR SESION
        ActionCard(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            titulo = "Cerrar sesión",
            descripcion = "Salir de tu cuenta"
        ) {
            onLogout()
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



