package com.example.remindmev2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.remindmev2.ui.panelTareas.DetalleTareaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TareaAdapter(
    private val listaTareas: MutableList<Tarea>,
    private val onTareaEliminada: () -> Unit = {}
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloTarea)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionTarea)
        val tvFechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun getItemCount(): Int = listaTareas.size

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]
        holder.tvTitulo.text = tarea.titulo
        holder.tvDescripcion.text = tarea.descripcion
        holder.tvFechaHora.text = "${tarea.fecha} ${tarea.hora}"

        // ✅ Al hacer clic en la tarjeta se abre la pantalla de detalle
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleTareaActivity::class.java)
            intent.putExtra("titulo", tarea.titulo)
            intent.putExtra("descripcion", tarea.descripcion)
            intent.putExtra("fechaHora", "${tarea.fecha} ${tarea.hora}")
            intent.putExtra("idTarea", tarea.id) // útil para futuras ediciones
            context.startActivity(intent)
        }

        // ⚠️ Eliminación de tareas con ventana de confirmación
        holder.btnEliminar.setOnClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar") { _, _ ->
                    eliminarTarea(tarea, position)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun eliminarTarea(tarea: Tarea, position: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseFirestore.getInstance()
                .collection("tareas")
                .document(user.uid)
                .collection("mis_tareas")
                .document(tarea.id)
                .delete()
                .addOnSuccessListener {
                    listaTareas.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listaTareas.size)
                    onTareaEliminada()
                }
                .addOnFailureListener {
                    // Manejo de error si es necesario
                }
        }
    }
}



