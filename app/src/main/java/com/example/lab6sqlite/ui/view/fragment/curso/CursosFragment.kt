package com.example.lab6sqlite.ui.view.fragment.curso

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.CursoDataBaseHelper
import com.example.lab6sqlite.database.CursosDeEstudianteDataBaseHelper
import com.example.lab6sqlite.database.EstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentCursosBinding
import com.example.lab6sqlite.modelo.Curso
import com.example.lab6sqlite.modelo.Estudiante
import com.example.lab6sqlite.ui.view.NavDrawActivity
import com.example.lab6sqlite.ui.view.fragment.estudiante.CrearEstudianteFragment
import com.example.lab6sqlite.ui.view.fragment.estudiante.EditarEstudianteFragment
import com.example.lab6sqlite.ui.view.fragment.estudiante.EstudianteFragment
import com.example.lab6sqlite.ui.view.fragment.estudiante.EstudiantesFragment
import com.example.lab6sqlite.ui.view.fragment.matricula.MatricularCursoFragment
import com.example.lab6sqlite.ui.view.recyclerView.curso.CursoAdapter
import com.example.lab6sqlite.ui.view.recyclerView.estudiante.EstudianteAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import kotlin.collections.ArrayList


class CursosFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var estudianteElegido: Estudiante? = null

    private var _binding: FragmentCursosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CursoAdapter
    private val cursos: ArrayList<Curso> = arrayListOf()

    private lateinit var cursoDataBaseHelper: CursoDataBaseHelper
    private lateinit var cursosDeEstudianteDataBaseHelper: CursosDeEstudianteDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            estudianteElegido = it.getSerializable(ARG_PARAM1) as Estudiante?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCursosBinding.inflate(inflater, container, false)

        cursoDataBaseHelper = CursoDataBaseHelper(activity!!)
        cursosDeEstudianteDataBaseHelper = CursosDeEstudianteDataBaseHelper(activity!!)

        val response = if (estudianteElegido == null) cursoDataBaseHelper.allCursos
        else cursosDeEstudianteDataBaseHelper.cursosDeEstudiante(estudianteElegido!!.id)

        if (response != null) {
            while (response.moveToNext()) {
                cursos.add(
                    Curso(
                        response.getString(0),
                        response.getString(1),
                        response.getInt(2)
                    )
                )
            }
        }

        initRecyclerView()
        setSearchBar()
        if (estudianteElegido == null) setRecyclerViewsItemsTouchHelper()
        else setRecyclerViewItemsTouchHelperMatricular()

        binding.apply {
            fab.setOnClickListener {
                if (estudianteElegido == null) {
                    (activity as NavDrawActivity).supportActionBar?.title = "Registrar Cursos"
                    swapFragments(
                        CrearCursoFragment.newInstance()
                    )
                } else {
                    (activity as NavDrawActivity).supportActionBar?.title =
                        "Matricular Curso a ${estudianteElegido!!.id}"
                    swapFragments(
                        MatricularCursoFragment.newInstance(estudianteElegido!!)
                    )
                }
            }
        }

        return binding.root
    }

    private fun getCursos() {
        val response = if (estudianteElegido == null) cursoDataBaseHelper.allCursos
        else cursosDeEstudianteDataBaseHelper.cursosDeEstudiante(estudianteElegido!!.id)

        if (response != null) {
            cursos.clear()

            while (response.moveToNext()) {
                cursos.add(
                    Curso(
                        response.getString(0),
                        response.getString(1),
                        response.getInt(2)
                    )
                )
            }
        } else cursos.clear()

        actualizarRecyclerView()
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@CursosFragment.context)
            adapter = CursoAdapter(cursos) { onItemSelected(it) }
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun actualizarRecyclerView() {
        binding.apply {
            adapter = CursoAdapter(cursos) { onItemSelected(it) }

            recyclerView.adapter = adapter
        }
    }

    private fun swapFragments(fragment: Fragment) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.contentMain, fragment
        )

        fragmentTransaction.commit()
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

    private fun eliminarBox(position: Int) {
        binding.apply {
            if (estudianteElegido == null) {
                AlertDialog.Builder(this@CursosFragment.context!!).apply {
                    setTitle("¿Está seguro de eliminar este curso?")
                    setMessage("Esta acción removerá el curso del sistema y es irreversible.")

                    setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                        val response = cursoDataBaseHelper.deleteCurso(cursos[position].id)

                        if (response != 0) {
                            getCursos()
                        } else {
                            Toast.makeText(
                                this@CursosFragment.context,
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
                            this@CursosFragment.context,
                            "Acción cancelada",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.show()
            } else {
                AlertDialog.Builder(this@CursosFragment.context!!).apply {
                    setTitle("¿Está seguro de desmatricular este curso?")
                    setMessage("Esta acción removerá al usuario del sistema y es irreversible.")

                    setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
                        if (cursosDeEstudianteDataBaseHelper.eliminarMatricula(
                                cursos[position].id,
                                estudianteElegido!!.id
                            )
                        ) {
                            getCursos()
                        } else {
                            Toast.makeText(
                                this@CursosFragment.context,
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
                            this@CursosFragment.context,
                            "Acción cancelada",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.show()
            }
        }
    }

    private fun setRecyclerViewItemsTouchHelperMatricular() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition

                Collections.swap(cursos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    eliminarBox(position)
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
                    this@CursosFragment.context,
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
                            this@CursosFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
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

                Collections.swap(cursos, fromPosition, toPosition)

                binding.apply {
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                }

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.RIGHT) {
                    binding.apply {
                        (activity as NavDrawActivity).supportActionBar?.title = "Editar Curso"

                        swapFragments(
                            EditarCursoFragment.newInstance(
                                cursos[position]
                            )
                        )
                    }
                } else {
                    eliminarBox(position)
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
                    this@CursosFragment.context,
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
                            this@CursosFragment.context!!,
                            R.color.red
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            this@CursosFragment.context!!,
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

    private fun onItemSelected(curso: Curso) {
        (activity as NavDrawActivity).supportActionBar?.title = "Visualizar Curso"

        swapFragments(
            CursoFragment.newInstance(
                curso, estudianteElegido
            )
        )
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
        fun newInstance(estudianteElegido: Estudiante? = null) = CursosFragment().apply {
            arguments = Bundle().apply {
                if (estudianteElegido != null) putSerializable(ARG_PARAM1, estudianteElegido)
            }
        }
    }
}