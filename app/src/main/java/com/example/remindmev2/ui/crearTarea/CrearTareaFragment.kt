package com.example.remindmev2.ui.crearTarea

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.remindmev2.R
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CrearTareaFragment : Fragment() {

    private lateinit var viewModel: CrearTareaViewModel
    private lateinit var etTitulo: TextInputEditText
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnFecha: Button
    private lateinit var btnHora: Button
    private lateinit var btnGuardar: Button
    private lateinit var tvFechaHora: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear_tarea, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrearTareaViewModel::class.java]

        // Inicializar vistas
        etTitulo = view.findViewById(R.id.etTitulo)
        etDescripcion = view.findViewById(R.id.etDescripcion)
        btnFecha = view.findViewById(R.id.btnSeleccionarFecha)
        btnHora = view.findViewById(R.id.btnSeleccionarHora)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        tvFechaHora = view.findViewById(R.id.tvFechaHoraSeleccionada)

        configurarObservadores()
        configurarListeners()
    }

    private fun configurarObservadores() {
        // Observar fecha seleccionada
        viewModel.fechaSeleccionada.observe(viewLifecycleOwner) { fecha ->
            actualizarTextoFechaHora()
        }

        // Observar hora seleccionada
        viewModel.horaSeleccionada.observe(viewLifecycleOwner) { hora ->
            actualizarTextoFechaHora()
        }

        // Observar validez del título
        viewModel.tituloValido.observe(viewLifecycleOwner) { valido ->
            btnGuardar.isEnabled = valido
        }

        // Observar cuando se guarda la tarea
        viewModel.tareaGuardada.observe(viewLifecycleOwner) { guardada ->
            if (guardada) {
                Toast.makeText(requireContext(), "✅ Tarea creada correctamente", Toast.LENGTH_SHORT).show()
                viewModel.resetEstado()
                // Limpiar formulario
                etTitulo.text?.clear()
                etDescripcion.text?.clear()
                // Navegar al panel de tareas
                findNavController().navigate(R.id.nav_home)
            }
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarListeners() {
        // RF05: Validar título en tiempo real
        etTitulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.validarTitulo(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnFecha.setOnClickListener { seleccionarFecha() }
        btnHora.setOnClickListener { seleccionarHora() }
        btnGuardar.setOnClickListener { guardarTarea() }
    }

    private fun seleccionarFecha() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val fecha = String.format("%02d/%02d/%04d", day, month + 1, year)
                viewModel.setFecha(fecha)
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun seleccionarHora() {
        val c = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val hora = String.format("%02d:%02d", hour, minute)
                viewModel.setHora(hora)
            },
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun actualizarTextoFechaHora() {
        val fecha = viewModel.fechaSeleccionada.value ?: ""
        val hora = viewModel.horaSeleccionada.value ?: ""

        tvFechaHora.text = if (fecha.isNotEmpty() && hora.isNotEmpty()) {
            "📅 $fecha  🕐 $hora"
        } else if (fecha.isNotEmpty()) {
            "📅 $fecha"
        } else if (hora.isNotEmpty()) {
            "🕐 $hora"
        } else {
            "Fecha y hora no seleccionadas"
        }
    }

    private fun guardarTarea() {
        val titulo = etTitulo.text?.toString() ?: ""
        val descripcion = etDescripcion.text?.toString() ?: ""
        viewModel.guardarTarea(titulo, descripcion)
    }

    private fun limpiarFormulario() {
        etTitulo.text?.clear()
        etDescripcion.text?.clear()
        tvFechaHora.text = "Fecha y hora no seleccionadas"
    }
}