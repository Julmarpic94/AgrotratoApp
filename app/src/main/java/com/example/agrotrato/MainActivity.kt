package com.example.agrotrato

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.agrotrato.ui.theme.AgroTratoSimpleTheme
import com.google.firebase.FirebaseApp
import com.example.agrotrato.navegacion.AppNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos Firebase
        FirebaseApp.initializeApp(this)

        // Cargamos la interfaz principal
        setContent {
            AgroTratoSimpleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigator()
                }
            }
        }
    }
}
