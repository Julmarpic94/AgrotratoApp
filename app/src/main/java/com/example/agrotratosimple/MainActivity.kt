package com.example.agrotratosimple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agrotratosimple.ui.theme.AgroTratoSimpleTheme
import androidx.compose.material3.Surface



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgroTratoSimpleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigator() // Aquí va la navegación central
                }
            }
        }
    }
}

//CREAMOS EL NAVEGADOR DE PANTALLAS
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Pantalla.Home.ruta) {
        composable(Pantalla.Login.ruta) {
            Login(
                onLoginSuccess = {
                    navController.navigate(Pantalla.Home.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                },
                onGoToRegistro = {
                    navController.navigate(Pantalla.Registro.ruta)
                }
            )
        }

        composable(Pantalla.Registro.ruta) {
            Registro(
                onRegistroSuccess = {
                    navController.popBackStack()
                },
                onGoToLogin = {
                    navController.navigate(Pantalla.Login.ruta)
                }
            )
        }

        composable(Pantalla.Home.ruta) {
            HomePantalla(
                onLogout = {
                    navController.navigate(Pantalla.Login.ruta) {
                        popUpTo(Pantalla.Home.ruta) { inclusive = true }
                    }
                }
            )
        }
    }
}
