package com.example.remindmev2

import android.os.Bundle
import android.util.Log//se importo pra ver si cargaaba la tarea
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActividadPanelTareas : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TareaAdapter
    private val listaTareas = mutableListOf<Tarea>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_panel_tareas)

        recyclerView = findViewById(R.id.recyclerTareas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TareaAdapter(listaTareas)
        recyclerView.adapter = adapter

        cargarTareas()
    }

    private fun cargarTareas() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseFirestore.getInstance()
            .collection("tareas")
            .document(user.uid)
            .collection("mis_tareas")
            .get()
            .addOnSuccessListener { documentos ->
                listaTareas.clear()
                for (doc in documentos) {
                    val tarea = Tarea(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        fecha = doc.getString("fecha") ?: "",
                        hora = doc.getString("hora") ?: ""
                    )
                    listaTareas.add(tarea)
                }
                Log.d("Firestore", "Tareas cargadas: ${listaTareas.size}")
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar las tareas", Toast.LENGTH_SHORT).show()
            }
    }
}

