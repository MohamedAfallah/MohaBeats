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
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentCancionesBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.MusicaViewModel
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial

@AndroidEntryPoint
class CancionesFrag : Fragment() {

    private var _binding: FragmentCancionesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterCanciones

    private val musicaViewModel: MusicaViewModel by lazy {
        ViewModelProvider(this).get(MusicaViewModel::class.java)
    }

    private var idUsuario: String = ""

    companion object {
        private const val ARG_ID_USUARIO = "arg_id_usuario"
        fun newInstance(idUsuario: String): CancionesFrag {
            val fragment = CancionesFrag()
            val bundle = Bundle()
            bundle.putString(ARG_ID_USUARIO, idUsuario)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancionesBinding.inflate(inflater, container, false)
        Log.d("FavoritosDebug", "CancionesFrag: onCreateView llamado.")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FavoritosDebug", "CancionesFrag: onViewCreated llamado.")

        idUsuario = arguments?.getString(ARG_ID_USUARIO) ?: "invitado"
        Log.d("FavoritosDebug", "CancionesFrag: idUsuario obtenido: $idUsuario")


        adapter = AdapterCanciones(
            idUsuario = idUsuario,
            onReproducirClick = { cancion ->
                Log.d("FavoritosDebug", "CancionesFrag: Reproducir click en ${cancion.nombreCancion}")
                reproducirCancion(cancion)
            },
            onFavoritoClick = { cancion, esFavoritoActual ->
                Log.d("FavoritosDebug", "CancionesFrag: Clic en favorito para canción ${cancion.nombreCancion} (ID: ${cancion.idCancion}). Era favorito: $esFavoritoActual")
                if (idUsuario.isEmpty() || idUsuario == "invitado") {
                    Log.d("FavoritosDebug", "CancionesFrag: Usuario invitado o ID vacío. Mostrando BottomSheet.")
                    (activity as? PaginaInicial)?.mostrarBottomSheetInvitado()
                } else {
                    if (esFavoritoActual) {
                        Log.d("FavoritosDebug", "CancionesFrag: Llamando a eliminarDeFavoritos para ${cancion.idCancion}")
                        musicaViewModel.eliminarDeFavoritos(idUsuario, cancion.idCancion)
                    } else {
                        Log.d("FavoritosDebug", "CancionesFrag: Llamando a anadirAFavoritos para ${cancion.idCancion}")
                        musicaViewModel.anadirAFavoritos(idUsuario, cancion.idCancion)
                    }
                }
            }
        )

        binding.recyclerViewCanciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCanciones.adapter = adapter

        musicaViewModel.musicaPaginada.observe(viewLifecycleOwner) { canciones ->
            val favoritasIds = musicaViewModel.favoritasIds.value ?: emptySet()
            Log.d("FavoritosDebug", "CancionesFrag: Observador musicaPaginada -> Recibidas ${canciones.size} canciones. Hay ${favoritasIds.size} favoritos. Actualizando adaptador.")
            adapter.actualizarDatos(canciones, favoritasIds)
        }

        musicaViewModel.favoritasIds.observe(viewLifecycleOwner) { nuevasFavoritasIds ->
            val cancionesActuales = musicaViewModel.musicaPaginada.value ?: emptyList()
            Log.d("FavoritosDebug", "CancionesFrag: Observador favoritasIds -> Nuevos favoritos: ${nuevasFavoritasIds.size}. Actualizando adaptador.")
            adapter.actualizarDatos(cancionesActuales, nuevasFavoritasIds)
        }

        binding.recyclerViewCanciones.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 2 >= totalItemCount) {
                    Log.d("FavoritosDebug", "CancionesFrag: Detectado fin de scroll. Cargando siguiente página.")
                    musicaViewModel.cargarPagina()
                }
            }
        })

        musicaViewModel.onCreate(idUsuario)
        Log.d("FavoritosDebug", "CancionesFrag: musicaViewModel.onCreate(${idUsuario}) llamado.")
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
        Log.d("FavoritosDebug", "CancionesFrag: onDestroyView llamado.")
        _binding = null
    }
}