package com.example.remindmev2.ui.crearTarea

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remindmev2.R

class CrearTareaFragment : Fragment() {

    companion object {
        fun newInstance() = CrearTareaFragment()
    }

    private val viewModel: CrearTareaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_crear_tarea, container, false)
    }
}