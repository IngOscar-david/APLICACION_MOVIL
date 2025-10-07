package com.example.remindmev2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class Actividad_Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_principal)

        // Referencias a los botones
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)
        val btnInfoApp: Button = findViewById(R.id.btnInfoApp)

        // Ir a Iniciar Sesión
        btnLogin.setOnClickListener {
            val intent = Intent(this, ActividadLogin::class.java)
            startActivity(intent)
        }

        // Ir a Registrar
        btnRegistro.setOnClickListener {
            val intent = Intent(this, ActividadRegistro::class.java)
            startActivity(intent)
        }

        // Ir a Acerca de la App
        btnInfoApp.setOnClickListener {
            val intent = Intent(this, ActividadInfoApp::class.java)
            startActivity(intent)
        }
    }
}
