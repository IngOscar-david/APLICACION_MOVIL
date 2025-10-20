package com.example.remindmev2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Actividad_Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_principal)

        // Referencias a los botones
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)
        val btnInfoApp: Button = findViewById(R.id.btnInfoApp)

        // Referencia al switch
        val switchModoOscuro: SwitchMaterial = findViewById(R.id.switchModoOscuro)

        // Listener del switch: activa o desactiva el modo oscuro
        switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

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
