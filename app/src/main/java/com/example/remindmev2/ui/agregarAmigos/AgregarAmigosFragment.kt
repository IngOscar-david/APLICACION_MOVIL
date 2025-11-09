package com.example.remindmev2.ui.compartirTarea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.remindmev2.R

class AgregarAmigosFragmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agregar_amigos, container, false)
    }
}
//acomodar los fragmentos para que se ven con el icono azul RECUERDAAA