package com.example.lab6sqlite.ui.view.fragment.estudiante

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.EstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentEstudiantesBinding
import com.example.lab6sqlite.modelo.Estudiante
import com.example.lab6sqlite.ui.view.NavDrawActivity
import com.example.lab6sqlite.ui.view.recyclerView.estudiante.EstudianteAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [EstudiantesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EstudiantesFragment : Fragment() {
    private var _binding: FragmentEstudiantesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EstudianteAdapter
    private val estudiantes: ArrayList<Estudiante> = arrayListOf()

    private lateinit var estudianteDataBaseHelper: EstudianteDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEstudiantesBinding.inflate(inflater, container, false)

        estudianteDataBaseHelper = EstudianteDataBaseHelper(activity!!)

        val response = estudianteDataBaseHelper.allData

        if (response != null) {
            while (response.moveToNext()) {
                estudiantes.add(
                    Estudiante(
                        response.getString(0),
                        response.getString(1),
                        response.getString(2),
                        response.getInt(3)
                    )
                )
            }
        }

        initRecyclerView()
        setSearchBar()
        setRecyclerViewsItemsTouchHelper()

        binding.apply {
            fab.setOnClickListener {
                (activity as NavDrawActivity).supportActionBar?.title = "Registrar Estudiante"
                swapFragments(
                    CrearEstudianteFragment.newInstance()
                )
            }
        }

        return binding.root
    }

    private fun getEstudiantes() {
        val response = estudianteDataBaseHelper.allData

        if (response != null) {
            estudiantes.clear()

            while (response.moveToNext()) {
                estudiantes.add(
                    Estudiante(
                        response.getString(0),
                        response.getString(1),
                        response.getString(2),
                        response.getInt(3)
                    )
                )
            }
        } else estudiantes.clear()

        actualizarRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@EstudiantesFragment.context)
            adapter = EstudianteAdapter(estudiantes) { onItemSelected(it) }
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun setSearchBar() {
        binding.apply {
            applicationSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })
        }
    }

    private fun actualizarRecyclerView() {
        binding.apply {
            adapter = EstudianteAdapter(estudiantes) { onItemSelected(it) }

            recyclerView.adapter = adapter
        }
    }

    private fun setRecyclerViewsItemsTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                Collections.swap(estudiantes, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavDrawActivity).supportActionBar?.title = "Editar Estudiante"

                        swapFragments(
                            EditarEstudianteFragment.newInstance(
                                estudiantes[position]
                            )
                        )
                    }
                } else {
                    binding.apply {
                        AlertDialog.Builder(this@EstudiantesFragment.context!!).apply {
                            setTitle("¿Está seguro de eliminar este usuario?")
                            setMessage("Esta acción removerá al usuario del sistema y es irreversible.")

                            setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                                val response = estudianteDataBaseHelper.deleteEstudiante(estudiantes[position].id)

                                if (response != 0) {
                                    getEstudiantes()
                                } else {
                                    Toast.makeText(
                                        this@EstudiantesFragment.context,
                                        "Error al eliminar",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                    actualizarRecyclerView()
                                }
                            }

                            setNegativeButton("Cancelar") { _: DialogInterface, _: Int ->
                                actualizarRecyclerView()

                                Toast.makeText(
                                    this@EstudiantesFragment.context,
                                    "Acción cancelada",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }.show()
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    this@EstudiantesFragment.context,
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            this@EstudiantesFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@EstudiantesFragment.context!!,
                            R.color.green
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.edit)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        binding.apply {
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun onItemSelected(estudiante: Estudiante) {
        (activity as NavDrawActivity).supportActionBar?.title = "Visualizar Estudiante"

        swapFragments(
            EstudianteFragment.newInstance(
                estudiante
            )
        )
    }

    private fun swapFragments(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, fragment
        )

        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EstudiantesFragment.
         */
        @JvmStatic
        fun newInstance() = EstudiantesFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}