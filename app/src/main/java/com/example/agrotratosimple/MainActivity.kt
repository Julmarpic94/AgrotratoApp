package com.example.agrotratosimple

import android.net.Uri
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AgroTratoSimpleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigator()
                }
            }
        }
    }
}

//CREAMOS EL NAVEGADOR DE PANTALLAS
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Pantalla.Login.ruta) {
        //NAVEGADOR LOGIN
        composable(Pantalla.Login.ruta) {
            Login(
                onLoginSuccess = { nombre, tipo ->
                    //Codificamos el nombre para evitar problemas con acentos
                    val nombreEncode = Uri.encode(nombre)
                    val tipoEncode = Uri.encode(tipo)

                    navController.navigate("home/$nombreEncode/$tipoEncode") {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                },
                onGoToRegistro = {
                    navController.navigate(Pantalla.Registro.ruta)
                }
            )
        }
        //NAVEGADOR REGISTRO
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
        //NAVEGADOR HOME
        composable(
            //Pasamos el nombre y lo
            route = "home/{nombre}/{tipo}",
            arguments = listOf(
                navArgument("nombre"){type = NavType.StringType},
                navArgument("tipo"){type = NavType.StringType},
                )
        ) { backStackEntry ->
            //Decodificamos los nombres para pasarlos al home
            val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre"))
            val tipo = Uri.decode(backStackEntry.arguments?.getString("tipo"))

            HomePantalla(
                nombre = nombre,
                tipo = tipo,
                onLogout = {
                    navController.navigate(Pantalla.Login.ruta) {
                        //Rompe los datos de navegación y sale
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
