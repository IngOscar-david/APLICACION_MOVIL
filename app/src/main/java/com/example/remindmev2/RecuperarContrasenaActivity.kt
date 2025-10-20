package com.example.remindmev2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RecuperarContrasenaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etCorreoRecuperar: EditText
    private lateinit var btnEnviarRecuperacion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        auth = FirebaseAuth.getInstance()

        etCorreoRecuperar = findViewById(R.id.etCorreoRecuperar)
        btnEnviarRecuperacion = findViewById(R.id.btnEnviarRecuperacion)

        btnEnviarRecuperacion.setOnClickListener {
            val email = etCorreoRecuperar.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enviar correo de recuperación con Firebase
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(this, "Se ha enviado un correo para restablecer tu contraseña.", Toast.LENGTH_LONG).show()
                    finish() // volver al login
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
