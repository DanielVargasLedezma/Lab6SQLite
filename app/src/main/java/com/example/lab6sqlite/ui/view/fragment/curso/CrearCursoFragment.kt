package com.example.lab6sqlite.ui.view.fragment.curso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.CursoDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentCrearCursoBinding
import com.example.lab6sqlite.databinding.FragmentCrearEstudianteBinding
import com.example.lab6sqlite.ui.view.NavDrawActivity
import com.example.lab6sqlite.ui.view.fragment.estudiante.CrearEstudianteFragment
import com.example.lab6sqlite.ui.view.fragment.estudiante.EstudiantesFragment

class CrearCursoFragment : Fragment() {
    private var _binding: FragmentCrearCursoBinding? = null
    private val binding get() = _binding!!

    private lateinit var cursoDataBaseHelper: CursoDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrearCursoBinding.inflate(inflater, container, false)

        cursoDataBaseHelper = CursoDataBaseHelper(activity!!)

        binding.apply {
            insertar.setOnClickListener {
                if (idCurso.text.isNotEmpty() && descripcionCurso.text.isNotEmpty() && creditosCurso.text.isNotEmpty()
                ) {
                    if (cursoDataBaseHelper.insertCurso(
                            idCurso.text.toString(),
                            descripcionCurso.text.toString(),
                            creditosCurso.text.toString().toInt()
                        )
                        != -1L
                    ) {
                        iniciarCursos()
                    } else {
                        Toast.makeText(
                            this@CrearCursoFragment.context,
                            "Error al insertar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@CrearCursoFragment.context,
                        "Campos sin rellenar",
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

    private fun iniciarCursos() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        (activity as NavDrawActivity).supportActionBar?.title = "Cursos Registrados"

        fragmentTransaction.replace(
            R.id.contentMain, CursosFragment.newInstance()
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
         * @return A new instance of fragment CrearEstudianteFragment.
         */
        @JvmStatic
        fun newInstance() = CrearCursoFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}