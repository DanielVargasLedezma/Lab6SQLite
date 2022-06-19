package com.example.lab6sqlite.ui.view.fragment.estudiante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.EstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentEditarEstudianteBinding
import com.example.lab6sqlite.modelo.Estudiante
import com.example.lab6sqlite.ui.view.NavDrawActivity

/**
 * A simple [Fragment] subclass.
 * Use the [EditarEstudianteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditarEstudianteFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var estudianteAVer: Estudiante? = null

    private var _binding: FragmentEditarEstudianteBinding? = null
    private val binding get() = _binding!!

    private lateinit var estudianteDataBaseHelper: EstudianteDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            estudianteAVer = it.getSerializable(ARG_PARAM1) as Estudiante?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditarEstudianteBinding.inflate(inflater, container, false)

        estudianteDataBaseHelper = EstudianteDataBaseHelper(activity!!)

        fullInputsByDefault()

        binding.apply {
            insertar.setOnClickListener {
                if (cedulaEstudiante.text.isNotEmpty() && nombreEstudiante.text.isNotEmpty() &&
                    apellidosEstudiante.text.isNotEmpty() && edadEstudiante.text.isNotEmpty()
                ) {
                    estudianteAVer!!.nombre = nombreEstudiante.text.toString()
                    estudianteAVer!!.apellidos = apellidosEstudiante.text.toString()
                    estudianteAVer!!.edad = edadEstudiante.text.toString().toInt()

                    val response = estudianteDataBaseHelper.editarEstudiante(
                        estudianteAVer!!.id,
                        estudianteAVer!!.nombre,
                        estudianteAVer!!.apellidos,
                        estudianteAVer!!.edad
                    )

                    if (response) {
                        iniciarCursos()
                    } else {
                        Toast.makeText(
                            this@EditarEstudianteFragment.context,
                            "Error al editar",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                } else {
                    Toast.makeText(
                        this@EditarEstudianteFragment.context,
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
            cedulaEstudiante.setText(estudianteAVer!!.id)
            nombreEstudiante.setText(estudianteAVer!!.nombre)
            apellidosEstudiante.setText(estudianteAVer!!.apellidos)
            edadEstudiante.setText(estudianteAVer!!.edad.toString())
        }
    }

    private fun iniciarCursos() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        (activity as NavDrawActivity).supportActionBar?.title = "Estudiantes Registrados"

        fragmentTransaction.replace(
            R.id.contentMain, EstudiantesFragment.newInstance()
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
         * @param estudiante Estudiante a ver.
         * @return A new instance of fragment EditarEstudianteFragment.
         */
        @JvmStatic
        fun newInstance(estudiante: Estudiante) =
            EditarEstudianteFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, estudiante)
                }
            }
    }
}