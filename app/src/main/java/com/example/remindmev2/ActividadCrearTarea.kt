package com.example.remindmev2

import android.content.Intent
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ActividadCrearTarea : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnFecha: Button
    private lateinit var btnHora: Button
    private lateinit var btnGuardar: Button

    private var fechaSeleccionada = ""
    private var horaSeleccionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_crear_tarea)

        // ✅ Verificación: el usuario debe estar autenticado antes de continuar
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, ActividadLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return
        }

        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnFecha = findViewById(R.id.btnSeleccionarFecha)
        btnHora = findViewById(R.id.btnSeleccionarHora)
        btnGuardar = findViewById(R.id.btnGuardar)

        // 🔹 Botón para ir al panel de tareas
        val btnVerTareas = findViewById<Button>(R.id.btnVerTareas)
        btnVerTareas.setOnClickListener {
            val intent = Intent(this, ActividadPanelTareas::class.java)
            startActivity(intent)
        }

        // 🔹 Habilitar el botón solo si hay título
        etTitulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnGuardar.isEnabled = s?.isNotEmpty() == true
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 🔹 Acciones de los botones
        btnFecha.setOnClickListener { seleccionarFecha() }
        btnHora.setOnClickListener { seleccionarHora() }
        btnGuardar.setOnClickListener { guardarTarea() }
    }

    private fun seleccionarFecha() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            fechaSeleccionada = String.format("%02d/%02d/%04d", day, month + 1, year)
            Toast.makeText(this, "Fecha: $fechaSeleccionada", Toast.LENGTH_SHORT).show()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun seleccionarHora() {
        val c = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            horaSeleccionada = String.format("%02d:%02d", hour, minute)
            Toast.makeText(this, "Hora: $horaSeleccionada", Toast.LENGTH_SHORT).show()
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    private fun guardarTarea() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Selecciona fecha y hora", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaHora: Date? = sdf.parse("$fechaSeleccionada $horaSeleccionada")
        if (fechaHora == null || fechaHora.before(Date())) {
            Toast.makeText(this, "La fecha/hora no puede ser en el pasado", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val id = db.collection("tareas").document().id
        val tarea = Tarea(id, titulo, descripcion, fechaSeleccionada, horaSeleccionada, "pendiente")

        db.collection("tareas")
            .document(userId)
            .collection("mis_tareas")
            .document(id)
            .set(tarea)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ Tarea creada correctamente", Toast.LENGTH_SHORT).show()

                // ✅ Corrección: redirigir al panel de tareas en lugar de hacer finish()
                val intent = Intent(this, ActividadPanelTareas::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

