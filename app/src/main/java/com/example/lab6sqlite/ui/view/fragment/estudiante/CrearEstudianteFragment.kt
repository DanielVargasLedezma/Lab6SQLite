package com.example.lab6sqlite.ui.view.fragment.estudiante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab6sqlite.R
import com.example.lab6sqlite.database.EstudianteDataBaseHelper
import com.example.lab6sqlite.databinding.FragmentCrearEstudianteBinding
import com.example.lab6sqlite.ui.view.NavDrawActivity

/**
 * A simple [Fragment] subclass.
 * Use the [CrearEstudianteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearEstudianteFragment : Fragment() {
    private var _binding: FragmentCrearEstudianteBinding? = null
    private val binding get() = _binding!!

    private lateinit var estudianteDataBaseHelper: EstudianteDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEstudianteBinding.inflate(inflater, container, false)

        estudianteDataBaseHelper = EstudianteDataBaseHelper(activity!!)

        binding.apply {
            insertar.setOnClickListener {
                if (cedulaEstudiante.text.isNotEmpty() && nombreEstudiante.text.isNotEmpty() &&
                    apellidosEstudiante.text.isNotEmpty() && edadEstudiante.text.isNotEmpty()
                ) {
                    if (estudianteDataBaseHelper.insertarEstudiante(
                            cedulaEstudiante.text.toString(),
                            nombreEstudiante.text.toString(),
                            apellidosEstudiante.text.toString(),
                            edadEstudiante.text.toString().toInt()
                        ) != -1L
                    ) {
                        iniciarEstudiantes()
                    } else {
                        Toast.makeText(
                            this@CrearEstudianteFragment.context,
                            "Error al insertar",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@CrearEstudianteFragment.context,
                        "Campos sin rellenar",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun iniciarEstudiantes() {
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
         * @return A new instance of fragment CrearEstudianteFragment.
         */
        @JvmStatic
        fun newInstance() = CrearEstudianteFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}