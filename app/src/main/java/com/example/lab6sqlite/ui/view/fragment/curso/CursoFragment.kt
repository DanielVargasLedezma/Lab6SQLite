package com.example.lab6sqlite.ui.view.fragment.curso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab6sqlite.R
import com.example.lab6sqlite.databinding.FragmentCursoBinding
import com.example.lab6sqlite.modelo.Curso
import com.example.lab6sqlite.modelo.Estudiante
import com.example.lab6sqlite.ui.view.NavDrawActivity

/**
 * A simple [Fragment] subclass.
 * Use the [CursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CursoFragment : Fragment() {
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private var cursosVisualizar: Curso? = null
    private var estudianteElegido: Estudiante? = null

    private var _binding: FragmentCursoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursosVisualizar = it.getSerializable(ARG_PARAM1) as Curso?
            estudianteElegido = it.getSerializable(ARG_PARAM2) as Estudiante?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCursoBinding.inflate(inflater, container, false)

        binding.apply {
            idCurso.setText(cursosVisualizar!!.id)
            descripcionCurso.setText(cursosVisualizar!!.descripcion)
            creditosCurso.setText(cursosVisualizar!!.creditos.toString())

            goBack.setOnClickListener {
                iniciarCursos()
            }
        }

        return binding.root
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
         * @param curso Curso a visualizar.
         * @param estudianteElegido Estudiante del cual se están viendo los cursos matriculados.
         * @return A new instance of fragment CursoFragment.
         */
        @JvmStatic
        fun newInstance(curso: Curso, estudianteElegido: Estudiante? = null) =
            CursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, curso)
                    if (estudianteElegido != null) putSerializable(ARG_PARAM2, estudianteElegido)
                }
            }
    }
}