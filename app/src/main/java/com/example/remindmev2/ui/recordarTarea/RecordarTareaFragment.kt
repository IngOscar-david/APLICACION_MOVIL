package com.example.remindmev2.ui.recordarTarea

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.remindmev2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class RecordarTareaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recordar_tarea, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvRecordarTareas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cargarTareas()
    }

    private fun cargarTareas() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("tareas")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val tareas = result.documents.map { doc ->
                    TareaRecordatorio(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "",
                        descripcion = doc.getString("descripcion") ?: ""
                    )
                }
                recyclerView.adapter = RecordarTareaAdapter(tareas) { tarea ->
                    mostrarDialogoRecordatorio(tarea)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoRecordatorio(tarea: TareaRecordatorio) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                calendar.set(year, month, day, hour, minute)
                programarNotificacion(tarea, calendar)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun programarNotificacion(tarea: TareaRecordatorio, calendar: Calendar) {
        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
            putExtra("titulo", tarea.titulo)
            putExtra("descripcion", tarea.descripcion)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            tarea.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(requireContext(), "Recordatorio programado", Toast.LENGTH_SHORT).show()
    }
}

data class TareaRecordatorio(
    val id: String,
    val titulo: String,
    val descripcion: String
)
