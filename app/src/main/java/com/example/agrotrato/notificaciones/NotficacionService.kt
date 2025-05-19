package com.example.agrotrato.notificaciones

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class NotificacionService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Mensaje recibido: ${message.notification?.title}")
        // Aquí puedes mostrar una notificación local o actualizar la UI
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
        // Puedes enviar el token al backend si lo necesitas
    }
}
