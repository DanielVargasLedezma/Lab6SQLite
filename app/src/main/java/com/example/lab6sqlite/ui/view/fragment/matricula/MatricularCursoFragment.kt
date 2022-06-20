package com.example.lab6sqlite.ui.view.fragment.matricula

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.CursoDataBaseHelper
import com.example.lab6sqlite.database.CursosDeEstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentMatricularCursoBinding
import com.example.lab6sqlite.modelo.Curso
import com.example.lab6sqlite.modelo.Estudiante
import com.example.lab6sqlite.ui.view.NavDrawActivity
import com.example.lab6sqlite.ui.view.fragment.curso.CursosFragment


/**
 * A simple [Fragment] subclass.
 * Use the [MatricularCursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatricularCursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var estudianteElegido: Estudiante? = null

    private var _binding: FragmentMatricularCursoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCurso: ArrayAdapter<Curso>

    private val cursos: ArrayList<Curso> = arrayListOf()

    private lateinit var cursoDataBaseHelper: CursoDataBaseHelper
    private lateinit var cursosDeEstudianteDataBaseHelper: CursosDeEstudianteDataBaseHelper

    private var idCursoSeleccionado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            estudianteElegido = it.getSerializable(ARG_PARAM1) as Estudiante?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatricularCursoBinding.inflate(inflater, container, false)

        cursoDataBaseHelper = CursoDataBaseHelper(activity!!)
        cursosDeEstudianteDataBaseHelper = CursosDeEstudianteDataBaseHelper(activity!!)

        val response = cursoDataBaseHelper.allCursos

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

        setAdapterCurso()

        binding.apply {
            insertar.setOnClickListener {
                if (idCursoSeleccionado.isNotEmpty()) {
                    if (cursosDeEstudianteDataBaseHelper.insertarMatricula(
                            idCursoSeleccionado,
                            estudianteElegido!!.id
                        )
                    ) {
                        iniciarCursos()
                    } else {
                        Toast.makeText(
                            this@MatricularCursoFragment.context,
                            "Error al matricular",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MatricularCursoFragment.context,
                        "Campos sin seleccionar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            goBack.setOnClickListener {
                iniciarCursos()
            }
        }

        return binding.root
    }

    private fun setAdapterCurso() {
        binding.apply {
            adapterCurso = ArrayAdapter<Curso>(
                this@MatricularCursoFragment.context!!,
                R.layout.spinner_list_curso_first
            )

            adapterCurso.setDropDownViewResource(R.layout.spinner_list_item)

            adapterCurso.add(Curso("- Seleccionar curso"))
            adapterCurso.addAll(cursos)

            cursoSpinner.adapter = adapterCurso

            cursoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    idCursoSeleccionado = if (position > 0) cursos[position - 1].id
                    else ""
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun iniciarCursos() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        if (estudianteElegido == null) (activity as NavDrawActivity).supportActionBar?.title = "Cursos Registrados"
        else (activity as NavDrawActivity).supportActionBar?.title = "Cursos Matriculados de ${estudianteElegido!!.id}"

        fragmentTransaction.replace(
            R.id.contentMain, CursosFragment.newInstance(estudianteElegido)
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
         * @param estudianteElegido Estudiante a matricular Cursos.
         * @return A new instance of fragment MatricularCursoFragment.
         */
        @JvmStatic
        fun newInstance(estudianteElegido: Estudiante) =
            MatricularCursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, estudianteElegido)
                }
            }
    }
}