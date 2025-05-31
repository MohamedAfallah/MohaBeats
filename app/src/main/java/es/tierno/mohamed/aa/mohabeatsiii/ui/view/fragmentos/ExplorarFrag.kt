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

class ExplorarFrag : Fragment(), BuscarFrag.BuscarFocusListener {

    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)

        // Cargar BuscarFrag en el contenedor fragBuscarContainer
        childFragmentManager.beginTransaction()
            .replace(R.id.fragBuscarContainer, BuscarFrag().apply {
                setBuscarFocusListener(this@ExplorarFrag)
            })
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
            Toast.makeText(requireContext(), "Clic en: ${categoria.nombre}", Toast.LENGTH_SHORT).show()

            val resultadoBusquedaFrag = ResultadoBusquedaFrag.newInstance(categoria)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragFullScreenContainer, resultadoBusquedaFrag)
                .addToBackStack(null)
                .commit()

            (parentFragment as? InicioFrag)?.showFullScreenContainer()
        }
    }

    // Implementación de la interface BuscarFocusListener
    override fun onSearchViewFocused() {
        // Mostrar ResultadoBusquedaFrag en el contenedor fullscreen
        val resultadoBusquedaFrag = ResultadoBusquedaFrag()

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragFullScreenContainer, resultadoBusquedaFrag)
            .addToBackStack(null)
            .commit()

        (parentFragment as? InicioFrag)?.showFullScreenContainer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



