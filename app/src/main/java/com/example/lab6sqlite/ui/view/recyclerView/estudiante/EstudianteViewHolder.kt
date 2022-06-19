package com.example.lab6sqlite.ui.view.recyclerView.estudiante

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6sqlite.databinding.ItemEstudianteBinding
import com.example.lab6sqlite.modelo.Estudiante

class EstudianteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemEstudianteBinding.bind(view)

    fun render(estudiante: Estudiante, onClickListener: (Estudiante) -> Unit) {
        binding.apply {
            cedulaEstudiante.text = estudiante.id
            nombreEstudiante.text = "${estudiante.nombre} ${estudiante.apellidos}"
            edadEstudiante.text = estudiante.edad.toString()
        }

        itemView.setOnClickListener {
            onClickListener(estudiante)
        }
    }
}