package com.example.remindmev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//este data class LO TENGO QUE CREAR EN UN ARCHIVO NUEVO PORQUE MAS ADELANTE ME PUDE DAR PROBLEMAS UN SOLO ARCHIVO A NIVEL DE com.example class tarea.kt y colocar este json
data class Tarea(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val estado: String = "pendiente"
)

class TareaAdapter(private val listaTareas: MutableList<Tarea>) :
    RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo = itemView.findViewById<TextView>(R.id.tvTituloTarea)
        val tvDescripcion = itemView.findViewById<TextView>(R.id.tvDescripcionTarea)
        val tvFechaHora = itemView.findViewById<TextView>(R.id.tvFechaHora)
        val btnEliminar = itemView.findViewById<Button>(R.id.btnEliminar)
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

        //Eliminar tarea
        holder.btnEliminar.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                FirebaseFirestore.getInstance()
                    .collection("tareas")
                    .document(user.uid)
                    .collection("tareasUsuario")
                    .document(tarea.id)
                    .delete()
                    .addOnSuccessListener {
                        listaTareas.removeAt(position)
                        notifyItemRemoved(position)
                    }
            }
        }
    }
}


