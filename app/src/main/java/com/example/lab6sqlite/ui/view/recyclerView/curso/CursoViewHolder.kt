package com.example.lab6sqlite.ui.view.recyclerView.curso

import android.content.DialogInterface.OnClickListener
import android.view.View
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6sqlite.databinding.CursoItemBinding
import com.example.lab6sqlite.modelo.Curso

class CursoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CursoItemBinding.bind(view)

    fun render(curso: Curso, onClickListener: (Curso) -> Unit){
        binding.apply{
            idCurso.text = curso.id
            descripcion.text = curso.descripcion
            creditosCurso.text = curso.creditos.toString()
        }
        itemView.setOnClickListener{
            onClickListener(curso)
        }
    }

}