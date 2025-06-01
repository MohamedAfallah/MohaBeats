package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentBusquedaBinding
import es.tierno.mohamed.aa.mohabeatsiii.data.model.CategoriasModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.ResultadoBusquedaViewModel

@AndroidEntryPoint
class ResultadoBusquedaFrag : Fragment() {

    companion object {
        private const val ARG_CATEGORIA = "arg_categoria"

        fun newInstance(categoria: CategoriasModel): ResultadoBusquedaFrag {
            val fragment = ResultadoBusquedaFrag()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CATEGORIA, categoria)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentBusquedaBinding? = null
    private val binding get() = _binding!!

    private var categoria: CategoriasModel? = null

    private val viewModel: ResultadoBusquedaViewModel by lazy {
        ViewModelProvider(this).get(ResultadoBusquedaViewModel::class.java)
    }

    private lateinit var adapter: AdapterCanciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoria = arguments?.getParcelable(ARG_CATEGORIA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusquedaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtGenero.text = categoria?.nombre ?: "Categoría no disponible"

        adapter = AdapterCanciones { cancion ->
            reproducirCancion(cancion)
        }
        binding.recyclerViewCancionesBusqueda.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCancionesBusqueda.adapter = adapter

        categoria?.let {
            viewModel.cargarCanciones(it.cod) // Ajusta el campo si es otro
        }

        viewModel.canciones.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarDatos(lista)
        }
    }

    private fun reproducirCancion(cancion: Musica) {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_URL, cancion.urlPreview)
        }
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}









