package com.example.agrotrato.navegacion

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agrotrato.vista.CrearSubastaVista
import com.example.agrotrato.vista.HomeVista
import com.example.agrotrato.vista.LoginVista
import com.example.agrotrato.vista.RegistroVista
import com.example.agrotrato.vista.VerSubastasVista
import com.example.agrotrato.vista.MisSubastasVista
import com.example.agrotrato.vista.MisPujasVista
import com.example.agrotrato.vista.PerfilVista
import com.example.agrotrato.vista.HacerPujaVista
import com.example.agrotrato.vista.NotificacionesVista
import com.example.agrotrato.vista.NotificacionesLeidas

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Pantalla.Login.ruta) {
        composable(Pantalla.Login.ruta) {
            LoginVista(
                onLoginSuccess = { nombre, tipo, uid ->
                    val nombreEncode = Uri.encode(nombre)
                    val tipoEncode = Uri.encode(tipo)
                    val uidEncode = Uri.encode(uid)

                    navController.navigate("home/$nombreEncode/$tipoEncode/$uidEncode") {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                },
                onGoToRegistro = {
                    navController.navigate(Pantalla.Registro.ruta)
                }
            )
        }

        composable(Pantalla.Registro.ruta) {
            RegistroVista(
                onRegistroSuccess = { navController.popBackStack() },
                onGoToLogin = { navController.navigate(Pantalla.Login.ruta) }
            )
        }

        composable(
            route = "home/{nombre}/{tipo}/{uid}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre") ?: "")
            val tipo = Uri.decode(backStackEntry.arguments?.getString("tipo") ?: "")
            val uid = backStackEntry.arguments?.getString("uid") ?: ""

            HomeVista(
                navController = navController,
                nombre = nombre,
                tipo = tipo,
                idUsuario = uid,
                onLogout = {
                    navController.navigate(Pantalla.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        //ACCESO A CREAR SUBASTA
        composable(
            route = "crearSubasta/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            CrearSubastaVista(
                idUsuario = idUsuario, // ✅ CORREGIDO
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }
        //ACCESO A VER SUBASTAS
        composable(
            route = "verSubastas/{idUsuario}/{tipoUsuario}",
            arguments = listOf(
                navArgument("idUsuario") { type = NavType.StringType },
                navArgument("tipoUsuario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""
            val tipoUsuario = Uri.decode(backStackEntry.arguments?.getString("tipoUsuario") ?: "")

            VerSubastasVista(
                navController = navController,
                idUsuario = idUsuario,
                tipoUsuario = tipoUsuario,
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }


        //VER MIS SUBASTAS
        composable(
            route = "misSubastas/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            MisSubastasVista(
                idUsuario = idUsuario,
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }

        //VER MIS PUJAS
        composable(
            route = "misPujas/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            MisPujasVista(
                idUsuario = idUsuario,
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }

        //NAVEGAR A MI PERFIL
        composable(
            route = "miPerfil/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            PerfilVista(
                idUsuario = idUsuario,
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }

        //VER NOTIFICACIONES
        composable(
            route = "misNotificaciones/{nombre}/{tipo}/{uid}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("tipo") { type = NavType.StringType },
                navArgument("uid") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre") ?: "")
            val tipo = Uri.decode(backStackEntry.arguments?.getString("tipo") ?: "")
            val uid = backStackEntry.arguments?.getString("uid") ?: ""

            NotificacionesVista(
                idUsuario = uid,
                onVolverHome = {
                    navController.navigate(Pantalla.Home.crearRuta(nombre, tipo, uid)) {
                        popUpTo(Pantalla.Home.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        //VER HISTORIAL DE NOTIFICACIONES
        composable(
            route = "notificacionesLeidas/{idUsuario}",
            arguments = listOf(navArgument("idUsuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            NotificacionesLeidas(
                idUsuario = idUsuario,
                onVolverHome = {
                    navController.popBackStack()
                }
            )
        }


        //HACER PUJA
        composable(
            route = "hacerPuja/{subastaId}/{idUsuario}",
            arguments = listOf(
                navArgument("subastaId") { type = NavType.StringType },
                navArgument("idUsuario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val subastaId = backStackEntry.arguments?.getString("subastaId") ?: ""
            val idUsuario = backStackEntry.arguments?.getString("idUsuario") ?: ""

            HacerPujaVista(
                subastaId = subastaId,
                idUsuario = idUsuario,
                onPujaExitosa = {
                    navController.popBackStack()
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
