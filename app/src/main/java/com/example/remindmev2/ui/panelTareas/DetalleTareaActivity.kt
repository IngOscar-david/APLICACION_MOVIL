package com.example.remindmev2.ui.panelTareas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.remindmev2.databinding.ActivityDetalleTareaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class DetalleTareaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleTareaBinding
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var tareaId: String
    private var titulo: String? = null
    private var descripcion: String? = null
    private var fechaHora: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir datos
        tareaId = intent.getStringExtra("idTarea") ?: ""
        titulo = intent.getStringExtra("titulo")
        descripcion = intent.getStringExtra("descripcion")
        fechaHora = intent.getStringExtra("fechaHora")

        // Mostrar datos
        binding.tvTituloDetalle.text = titulo
        binding.tvDescripcionDetalle.text = descripcion
        binding.tvFechaHoraDetalle.text = fechaHora

        // Botón volver
        binding.btnVolver.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Botón editar
        binding.btnEditar.setOnClickListener {
            mostrarDialogoEditar()
        }
    }

    private fun mostrarDialogoEditar() {
        val dialogView = layoutInflater.inflate(com.example.remindmev2.R.layout.dialog_editar_tarea, null)

        val etTitulo = dialogView.findViewById<EditText>(com.example.remindmev2.R.id.etTituloEditar)
        val etDescripcion = dialogView.findViewById<EditText>(com.example.remindmev2.R.id.etDescripcionEditar)

        etTitulo.setText(titulo)
        etDescripcion.setText(descripcion)

        val builder = AlertDialog.Builder(this)
            .setTitle("Editar tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTitulo = etTitulo.text.toString()
                val nuevaDescripcion = etDescripcion.text.toString()

                if (nuevoTitulo.isNotEmpty() && user != null) {
                    val tareaRef = db.collection("tareas")
                        .document(user.uid)
                        .collection("mis_tareas")
                        .document(tareaId)

                    tareaRef.update(
                        mapOf(
                            "titulo" to nuevoTitulo,
                            "descripcion" to nuevaDescripcion
                        )
                    ).addOnSuccessListener {
                        binding.tvTituloDetalle.text = nuevoTitulo
                        binding.tvDescripcionDetalle.text = nuevaDescripcion
                        Toast.makeText(this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)

        builder.show()
    }
}

