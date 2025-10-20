package com.example.remindmev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActividadLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_login) // conecta con tu layout XML

        // Referencias a los elementos del layout
        val etCorreo = findViewById<EditText>(R.id.etCorreoLogin)
        val etContrasena = findViewById<EditText>(R.id.etContrasenaLogin)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val tvOlvidarContrasena = findViewById<TextView>(R.id.tvOlvidarContrasena)

        // 🔹 Acción del botón "Iniciar Sesión" (si ya tienes tu lógica de login, déjala aquí)
        btnIniciarSesion.setOnClickListener {
            // Aquí va tu código para iniciar sesión con Firebase
        }

        // 🔹 Acción del texto "¿Olvidaste tu contraseña?"
        tvOlvidarContrasena.setOnClickListener {
            val intent = Intent(this, RecuperarContrasenaActivity::class.java)
            startActivity(intent)
        }
    }
}

