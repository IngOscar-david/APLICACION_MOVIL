package com.example.remindmev2.ui.recordarTarea

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remindmev2.R

class RecordarTareaAdapter(
    private val tareas: List<TareaRecordatorio>,
    private val onItemClick: (TareaRecordatorio) -> Unit
) : RecyclerView.Adapter<RecordarTareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTituloTarea)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcionTarea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recordar_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.titulo.text = tarea.titulo
        holder.descripcion.text = tarea.descripcion
        holder.itemView.setOnClickListener { onItemClick(tarea) }
    }

    override fun getItemCount() = tareas.size
}
