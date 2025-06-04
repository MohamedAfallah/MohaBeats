package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentDescargasHistorialBinding // Asegúrate de que esta importación sea correcta

class Historial : Fragment() {

    private var _binding: FragmentDescargasHistorialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescargasHistorialBinding.inflate(inflater, container, false)
        val view = binding.root

        // Establece el título específico para Historial
        binding.txtTitulo.text = getString(R.string.txtHistorial)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}