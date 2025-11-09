package com.example.remindmev2.ui.crearTarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.remindmev2.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CrearTareaViewModel : ViewModel() {

    private val _fechaSeleccionada = MutableLiveData<String>()
    val fechaSeleccionada: LiveData<String> = _fechaSeleccionada

    private val _horaSeleccionada = MutableLiveData<String>()
    val horaSeleccionada: LiveData<String> = _horaSeleccionada

    private val _tituloValido = MutableLiveData<Boolean>()
    val tituloValido: LiveData<Boolean> = _tituloValido

    private val _tareaGuardada = MutableLiveData<Boolean>()
    val tareaGuardada: LiveData<Boolean> = _tareaGuardada

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _fechaSeleccionada.value = ""
        _horaSeleccionada.value = ""
        _tituloValido.value = false
        _tareaGuardada.value = false
    }

    fun setFecha(fecha: String) {
        _fechaSeleccionada.value = fecha
    }

    fun setHora(hora: String) {
        _horaSeleccionada.value = hora
    }

    fun validarTitulo(titulo: String) {
        _tituloValido.value = titulo.trim().isNotEmpty()
    }

    fun guardarTarea(titulo: String, descripcion: String) {
        val fecha = _fechaSeleccionada.value ?: ""
        val hora = _horaSeleccionada.value ?: ""

        // Validar que se haya seleccionado fecha y hora
        if (fecha.isEmpty() || hora.isEmpty()) {
            _error.value = "Selecciona fecha y hora"
            return
        }

        // Validar que la fecha/hora no sea en el pasado
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaHora: Date? = try {
            sdf.parse("$fecha $hora")
        } catch (e: Exception) {
            null
        }

        if (fechaHora == null || fechaHora.before(Date())) {
            _error.value = "La fecha/hora no puede ser en el pasado"
            return
        }

        // Obtener el usuario actual
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            _error.value = "Usuario no autenticado"
            return
        }

        // Crear la tarea en Firestore
        val db = FirebaseFirestore.getInstance()
        val id = db.collection("tareas").document().id
        val tarea = Tarea(
            id = id,
            titulo = titulo.trim(),
            descripcion = descripcion.trim(),
            fecha = fecha,
            hora = hora,
            estado = "pendiente"
        )

        db.collection("tareas")
            .document(userId)
            .collection("mis_tareas")
            .document(id)
            .set(tarea)
            .addOnSuccessListener {
                _tareaGuardada.value = true
            }
            .addOnFailureListener { e ->
                _error.value = "Error al guardar: ${e.message}"
            }
    }

    fun resetEstado() {
        _tareaGuardada.value = false
        _fechaSeleccionada.value = ""
        _horaSeleccionada.value = ""
        _tituloValido.value = false
    }
}