package com.example.lab6sqlite.ui.view.fragment.curso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab6sqlite.databinding.FragmentCursoBinding
import com.example.lab6sqlite.modelo.Curso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CursoFragment : Fragment() {
    private  val ARG_PARAM1 = "param1"

    private var cursosVisualizar: Curso? = null

    private var _binding: FragmentCursoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cursosVisualizar = it.getSerializable(ARG_PARAM1) as Curso?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentCursoBinding.inflate(inflater, container, false)
       binding.apply {
           idCurso.setText(cursosVisualizar!!.id)
           descripcionCurso.setText(cursosVisualizar!!.descripcion)
           creditosCurso.setText(cursosVisualizar!!.creditos.toString())
       }
        return  binding.root
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
         * @return A new instance of fragment CursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(curso: Curso) =
            CursoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, curso)

                }
            }
    }
}