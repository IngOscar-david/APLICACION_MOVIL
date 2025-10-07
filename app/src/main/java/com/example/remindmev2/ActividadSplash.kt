package com.example.remindmev2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ActividadSplash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_splash)

        // Espera 2 segundos y abre la actividad principal
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Actividad_Principal::class.java)
            startActivity(intent)
            finish() // Para que no regrese al splash al presionar "atrás"
        }, 2000) // tiempo en milisegundos
    }
}
