package com.example.remindmev2.ui.panelTareas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.remindmev2.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PanelTareasViewModel : ViewModel() {

    private val _listaTareas = MutableLiveData<MutableList<Tarea>>()
    val listaTareas: LiveData<MutableList<Tarea>> = _listaTareas

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _listaTareas.value = mutableListOf()
        cargarTareas()
    }

    fun cargarTareas() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            _error.value = "No hay usuario autenticado"
            return
        }

        _cargando.value = true

        FirebaseFirestore.getInstance()
            .collection("tareas")
            .document(user.uid)
            .collection("mis_tareas")
            .get()
            .addOnSuccessListener { documentos ->
                val tareas = mutableListOf<Tarea>()
                for (doc in documentos) {
                    val tarea = Tarea(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        fecha = doc.getString("fecha") ?: "",
                        hora = doc.getString("hora") ?: "",
                        estado = doc.getString("estado") ?: "pendiente"
                    )
                    tareas.add(tarea)
                }
                _listaTareas.value = tareas
                _cargando.value = false
            }
            .addOnFailureListener { e ->
                _error.value = "Error al cargar las tareas: ${e.message}"
                _cargando.value = false
            }
    }
}