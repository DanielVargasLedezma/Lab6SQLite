package com.example.lab6sqlite.ui.view.recyclerView.estudiante

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6sqlite.R
import com.example.lab6sqlite.modelo.Estudiante
import java.util.*
import kotlin.collections.ArrayList

class EstudianteAdapter(
    private val estudiantes: ArrayList<Estudiante>,
    private val onClickListener: (Estudiante) -> Unit
) : RecyclerView.Adapter<EstudianteViewHolder>(), Filterable {
    private var itemsList: ArrayList<Estudiante> = estudiantes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        return EstudianteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_estudiante, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        holder.render(itemsList[position], onClickListener)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                itemsList = if (charSearch.isEmpty()) {
                    estudiantes
                } else {
                    val resultList = ArrayList<Estudiante>()

                    for (row in estudiantes) {
                        if (row.id.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault())) ||
                            row.nombre.lowercase(Locale.getDefault())
                                .contains(charSearch.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(row)
                        }
                    }

                    resultList
                }

                val filterResults = FilterResults()

                filterResults.values = itemsList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemsList = results?.values as ArrayList<Estudiante>
                notifyDataSetChanged()
            }
        }
    }
}