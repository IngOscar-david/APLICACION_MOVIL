package com.example.remindmev2.ui.recordarTarea

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.remindmev2.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "Tarea"
        val descripcion = intent.getStringExtra("descripcion") ?: "Tienes algo pendiente"

        val builder = NotificationCompat.Builder(context, "recordatorios")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(titulo)
            .setContentText(descripcion)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // ✅ Verificar permiso antes de mostrar la notificación
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context)
                .notify(System.currentTimeMillis().toInt(), builder.build())
        } else {
            // Log para depurar si el permiso no está concedido
            Log.w("NotificationReceiver", "No se tiene permiso POST_NOTIFICATIONS")
        }
    }
}

