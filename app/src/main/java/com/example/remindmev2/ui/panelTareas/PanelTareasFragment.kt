package com.example.remindmev2.ui.panelTareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.remindmev2.R
import com.example.remindmev2.TareaAdapter

class PanelTareasFragment : Fragment() {

    private lateinit var viewModel: PanelTareasViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoTareas: TextView
    private lateinit var adapter: TareaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_panel_tareas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PanelTareasViewModel::class.java]

        // Inicializar vistas
        recyclerView = view.findViewById(R.id.recyclerTareas)
        tvNoTareas = view.findViewById(R.id.tvNoTareas)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TareaAdapter(mutableListOf()) {
            // Callback cuando se elimina una tarea
            viewModel.cargarTareas()
        }
        recyclerView.adapter = adapter

        configurarObservadores()
    }

    private fun configurarObservadores() {
        // Observar lista de tareas
        viewModel.listaTareas.observe(viewLifecycleOwner) { tareas ->
            if (tareas.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvNoTareas.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                tvNoTareas.visibility = View.GONE

                // Actualizar adapter con las nuevas tareas
                adapter = TareaAdapter(tareas) {
                    viewModel.cargarTareas()
                }
                recyclerView.adapter = adapter
            }
        }

        // Observar estado de carga
        viewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            // Aquí podrías mostrar un ProgressBar si lo deseas
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar tareas al volver al fragment
        viewModel.cargarTareas()
    }
}
