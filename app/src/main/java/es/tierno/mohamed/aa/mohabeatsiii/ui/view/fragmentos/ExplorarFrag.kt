package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.data.provider.CategoriasProvider
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentExplorarBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_categorias.AdapterCategorias

class ExplorarFrag : Fragment() {
    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragBuscarContainer, BuscarFrag())
            .addToBackStack(null)
            .commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCategorias = CategoriasProvider.obtenerCategorias()

        binding.recyclerViewCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerViewCategorias.setHasFixedSize(true)
        binding.recyclerViewCategorias.clipToPadding = false
        binding.recyclerViewCategorias.setPadding(32, 0, 32, 0)

        binding.recyclerViewCategorias.adapter = AdapterCategorias(listaCategorias) { categoria ->
            // Aquí manejas el click en una categoría, por ejemplo:
            Toast.makeText(requireContext(), "Clic en: ${categoria.nombre}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
