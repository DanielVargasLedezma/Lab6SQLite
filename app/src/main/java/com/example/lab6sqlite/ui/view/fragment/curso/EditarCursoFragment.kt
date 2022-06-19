package com.example.lab6sqlite.ui.view.fragment.curso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.CursoDataBaseHelper
import com.example.lab6sqlite.database.EstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentEditarCursoBinding
import com.example.lab6sqlite.databinding.FragmentEditarEstudianteBinding
import com.example.lab6sqlite.modelo.Curso
import com.example.lab6sqlite.ui.view.NavDrawActivity
import com.example.lab6sqlite.ui.view.fragment.estudiante.EstudiantesFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [EditarCursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarCursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private var cursoVisualizar: Curso? = null

    private var _binding: FragmentEditarCursoBinding? = null
    private val binding get() = _binding!!

    private lateinit var cursoDataBaseHelper: CursoDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursoVisualizar = it.getSerializable(ARG_PARAM1)  as Curso?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditarCursoBinding.inflate(inflater, container, false)
        cursoDataBaseHelper = CursoDataBaseHelper(activity!!)

        fullInputsByDefault()

        binding.apply {
            insertar.setOnClickListener {
                if (idCurso.text.isNotEmpty() && descripcionCurso.text.isNotEmpty() && creditosCurso.text.isNotEmpty()
                ) {
                    cursoVisualizar!!.id = idCurso.text.toString()
                    cursoVisualizar!!.descripcion = descripcionCurso.text.toString()
                    cursoVisualizar!!.creditos = creditosCurso.text.toString().toInt()

                    val response = cursoDataBaseHelper.updateCurso(
                        cursoVisualizar!!.id,
                        cursoVisualizar!!.descripcion,
                        cursoVisualizar!!.creditos

                    )
                } else {
                    Toast.makeText(
                        this@EditarCursoFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            descartar.setOnClickListener {
                fullInputsByDefault()
            }
        }

        return binding.root
    }
    private fun fullInputsByDefault() {
        binding.apply {
            idCurso.setText(cursoVisualizar!!.id)
            descripcionCurso.setText(cursoVisualizar!!.descripcion)
            creditosCurso.setText(cursoVisualizar!!.creditos)
        }
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditarCursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(curso: Curso) =
            EditarCursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, curso)

                }
            }
    }
}