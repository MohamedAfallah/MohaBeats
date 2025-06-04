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
import es.tierno.mohamed.aa.mohabeatsiii.R

@AndroidEntryPoint
class ResultadoBusquedaFrag : Fragment() {

    companion object {
        private const val ARG_CATEGORIA = "arg_categoria"
        private const val ARG_TEXTO = "arg_texto"
        private const val ARG_ID_USUARIO = "arg_id_usuario"

        fun newInstance(categoria: CategoriasModel, idUsuario: String): ResultadoBusquedaFrag {
            val fragment = ResultadoBusquedaFrag()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CATEGORIA, categoria)
            bundle.putString(ARG_ID_USUARIO, idUsuario)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstanceTexto(texto: String, idUsuario: String): ResultadoBusquedaFrag {
            val fragment = ResultadoBusquedaFrag()
            val bundle = Bundle()
            bundle.putString(ARG_TEXTO, texto)
            bundle.putString(ARG_ID_USUARIO, idUsuario)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentBusquedaBinding? = null
    private val binding get() = _binding!!

    private var categoria: CategoriasModel? = null
    private var texto: String? = null
    private var idUsuario: String = "" // Añadido para el ID de usuario

    private val viewModel: ResultadoBusquedaViewModel by lazy {
        ViewModelProvider(this)[ResultadoBusquedaViewModel::class.java]
    }

    private lateinit var adapter: AdapterCanciones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoria = arguments?.getParcelable(ARG_CATEGORIA)
        texto = arguments?.getString(ARG_TEXTO)
        idUsuario = arguments?.getString(ARG_ID_USUARIO) ?: "invitado"
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

        adapter = AdapterCanciones(
            idUsuario = idUsuario,
            onReproducirClick = { cancion -> reproducirCancion(cancion) },
            onFavoritoClick = { cancion, esFavoritoActual ->
                if (idUsuario.isEmpty() || idUsuario == "invitado") {
                    (activity as? PaginaInicial)?.mostrarBottomSheetInvitado()
                } else {
                    if (esFavoritoActual) {
                        viewModel.eliminarDeFavoritos(idUsuario, cancion.idCancion)
                    } else {
                        viewModel.anadirAFavoritos(idUsuario, cancion.idCancion)
                    }
                }
            }
        )
        binding.recyclerViewCancionesBusqueda.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCancionesBusqueda.adapter = adapter

        // Llamada a onCreate del ViewModel para inicializar el ID de usuario y cargar favoritos
        viewModel.onCreate(idUsuario)

        if (categoria != null) {
            binding.txtGenero.text = categoria?.nombre ?: "Categoría no disponible"
            viewModel.cargarCanciones(categoria!!.cod)
        } else if (!texto.isNullOrEmpty()) {
            binding.txtGenero.text = "Resultados para \"$texto\""
            viewModel.buscarCanciones(texto!!)
            Log.d("Luffy", texto!!)
        }

        viewModel.canciones.observe(viewLifecycleOwner) { lista ->
            val favoritasIds = viewModel.favoritasIds.value ?: emptySet()
            adapter.actualizarDatos(lista, favoritasIds)
            if (lista.isEmpty()) {
                binding.txtGenero.text = "No se encontraron resultados"
            }
        }

        viewModel.favoritasIds.observe(viewLifecycleOwner) { nuevasFavoritasIds ->
            val cancionesActuales = viewModel.canciones.value ?: emptyList()
            adapter.actualizarDatos(cancionesActuales, nuevasFavoritasIds)
        }
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCanciones = adapter.obtenerDatos()
        val posicion = listaCanciones.indexOf(cancion).coerceAtLeast(0)

        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putParcelableArrayListExtra(MusicService.EXTRA_PLAYLIST, ArrayList(listaCanciones))
            putExtra("EXTRA_START_INDEX", posicion)
        }
        requireContext().startService(intent)

        (activity as? PaginaInicial)?.let { paginaInicial ->
            val miniContenedor = paginaInicial.findViewById<View>(R.id.reproductorMiniContainer)
            miniContenedor?.visibility = View.GONE

            val contenedor = paginaInicial.findViewById<View>(R.id.reproductorContainer)
            contenedor?.visibility = View.VISIBLE

            val reproductorFrag = ReproductorFrag.newInstance(cancion)
            paginaInicial.supportFragmentManager.beginTransaction()
                .add(R.id.reproductorContainer, reproductorFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}











