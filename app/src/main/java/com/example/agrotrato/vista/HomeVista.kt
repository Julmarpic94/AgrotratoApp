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

@Composable
fun HomeVista(
    navController: NavController,
    nombre: String,
    tipo: String,
    idUsuario: String,
    onLogout: () -> Unit
) {
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

        //CONDICIONAL PARA MOSTRAR UNOS CARDS  U OTROS
        if (tipo == "VENDEDOR") {
            ActionCard(
                icon = Icons.Default.AddCircle,
                titulo = "Crear subasta",
                descripcion = "Publica un nuevo producto para vender"
            ) {
                navController.navigate(Pantalla.CrearSubasta.crearRuta(idUsuario))
            }

        }
        //MOSTRAR TODAS LAS SUBASTAS
        ActionCard(
            Icons.Default.Search,
            "Explorar Subastas",
            "Consulta las subastas activas") {
            navController.navigate(Pantalla.VerSubastas.crearRuta(idUsuario))

        }

        if (tipo == "COMPRADOR") {
            ActionCard(Icons.Default.ShoppingCart, "Mis Pujas", "Gestiona el estado de tus pujas") {
                navController.navigate(Pantalla.MisPujas.crearRuta(idUsuario))
            }
        } else {
            ActionCard(
                Icons.Default.ShoppingCart,
                "Mis Subastas",
                "Gestiona el estado de tus subastas") {
                navController.navigate(Pantalla.MisSubastas.crearRuta(idUsuario))
            }
        }

        if (tipo == "VENDEDOR") {
            ActionCard(
                Icons.Default.Home,
                "Historial de Subastas",
                "Revisas tu historial de subastas") {

            }
        } else {
            ActionCard(Icons.Default.Home, "Historial de Pujas", "Revisa tu historial de pujas") {}
        }

        ActionCard(
            Icons.Default.AccountCircle,
            "Perfil",
            "Gestiona tu perfil") {
            navController.navigate(Pantalla.MisPerfil.crearRuta(idUsuario))
        }

        ActionCard(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión", "Salir de tu cuenta") {
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
                Text(text = titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Amarillo)
                Text(text = descripcion, fontSize = 14.sp, color = Blanco)
            }
        }
    }
}
