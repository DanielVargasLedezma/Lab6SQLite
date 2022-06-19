package com.example.lab6sqlite.ui.view.fragment.estudiante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab6sqlite.R
import com.example.lab6sqlite.databinding.FragmentEstudianteBinding
import com.example.lab6sqlite.modelo.Estudiante

/**
 * A simple [Fragment] subclass.
 * Use the [EstudianteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EstudianteFragment : Fragment() {
    private val ARG_PARAM1 = "param1"

    private var estudianteAVer: Estudiante? = null

    private var _binding: FragmentEstudianteBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentEstudianteBinding.inflate(inflater, container, false)

        binding.apply {
            cedulaEstudiante.setText(estudianteAVer!!.id)
            nombreEstudiante.setText("${estudianteAVer!!.nombre} ${estudianteAVer!!.apellidos}")
            edadEstudiante.setText(estudianteAVer!!.edad.toString())

            cursos.setOnClickListener {
                //Codigo de cargar cursos matriculados de este estudiante
            }
        }

        return binding.root
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
         * @return A new instance of fragment EstudianteFragment.
         */
        @JvmStatic
        fun newInstance(estudiante: Estudiante) =
            EstudianteFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, estudiante)
                }
            }
    }
}