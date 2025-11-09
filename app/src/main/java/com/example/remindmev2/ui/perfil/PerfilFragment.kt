package com.example.remindmev2.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.remindmev2.R
import com.google.firebase.auth.FirebaseAuth

class PerfilFragment : Fragment() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var tvNombrePerfil: TextView
    private lateinit var tvCorreoPerfil: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar vistas
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil)
        tvNombrePerfil = view.findViewById(R.id.tvNombrePerfil)
        tvCorreoPerfil = view.findViewById(R.id.tvCorreoPerfil)

        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Mostrar correo
            tvCorreoPerfil.text = user.email ?: "Sin correo"

            // Mostrar nombre (si está disponible) o usar el correo
            val nombre = user.displayName
            tvNombrePerfil.text = if (!nombre.isNullOrEmpty()) {
                nombre
            } else {
                // Extraer nombre del correo antes del @
                user.email?.substringBefore("@") ?: "Usuario"
            }

            // La foto por defecto ya está en el XML
            // Si el usuario tiene foto en Firebase, puedes cargarla aquí
            // Glide.with(this).load(user.photoUrl).into(ivFotoPerfil)
        }
    }
}