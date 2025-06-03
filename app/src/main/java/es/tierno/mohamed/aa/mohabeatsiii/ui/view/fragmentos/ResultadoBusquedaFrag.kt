package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.ReproductorFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.ResultadoBusquedaViewModel

@AndroidEntryPoint
class ResultadoBusquedaFrag : Fragment() {

    companion object {
        private const val ARG_CATEGORIA = "arg_categoria"
        private const val ARG_TEXTO = "arg_texto"

        fun newInstance(categoria: CategoriasModel): ResultadoBusquedaFrag {
            val fragment = ResultadoBusquedaFrag()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CATEGORIA, categoria)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstanceTexto(texto: String): ResultadoBusquedaFrag {
            val fragment = ResultadoBusquedaFrag()
            val bundle = Bundle()
            bundle.putString(ARG_TEXTO, texto)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentBusquedaBinding? = null
    private val binding get() = _binding!!

    private var categoria: CategoriasModel? = null
    private var texto: String? = null

    private val viewModel: ResultadoBusquedaViewModel by lazy {
        ViewModelProvider(this)[ResultadoBusquedaViewModel::class.java]
    }

    private lateinit var adapter: AdapterCanciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoria = arguments?.getParcelable(ARG_CATEGORIA)
        texto = arguments?.getString(ARG_TEXTO)
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

        adapter = AdapterCanciones { cancion -> reproducirCancion(cancion) }
        binding.recyclerViewCancionesBusqueda.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCancionesBusqueda.adapter = adapter

        if (categoria != null) {
            binding.txtGenero.text = categoria?.nombre ?: "Categoría no disponible"
            viewModel.cargarCanciones(categoria!!.cod)
        } else if (!texto.isNullOrEmpty()) {
            binding.txtGenero.text = "Resultados para \"$texto\""
            viewModel.buscarCanciones(texto!!)
            Log.d("Luffy", texto!!)
        }

        viewModel.canciones.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarDatos(lista)
            if (lista.isEmpty()) {
                binding.txtGenero.text = "No se encontraron resultados"
            }
        }
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCanciones = adapter.obtenerDatos()
        val posicion = listaCanciones.indexOf(cancion).coerceAtLeast(0)

        // Iniciar el servicio con lista completa y posición
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putParcelableArrayListExtra(MusicService.EXTRA_PLAYLIST, ArrayList(listaCanciones))
            putExtra("EXTRA_START_INDEX", posicion)
        }
        requireContext().startService(intent)

        // Ocultar reproductor minimizado y mostrar reproductor completo
        (activity as? PaginaInicial)?.let { paginaInicial ->
            val miniContenedor = paginaInicial.findViewById<View>(es.tierno.mohamed.aa.mohabeatsiii.R.id.reproductorMiniContainer)
            miniContenedor?.visibility = View.GONE

            val contenedor = paginaInicial.findViewById<View>(es.tierno.mohamed.aa.mohabeatsiii.R.id.reproductorContainer)
            contenedor?.visibility = View.VISIBLE

            val reproductorFrag = ReproductorFrag.newInstance(cancion)
            paginaInicial.supportFragmentManager.beginTransaction()
                .add(es.tierno.mohamed.aa.mohabeatsiii.R.id.reproductorContainer, reproductorFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}











