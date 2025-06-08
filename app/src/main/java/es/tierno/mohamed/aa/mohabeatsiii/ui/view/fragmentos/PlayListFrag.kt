package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentPlayListBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.PlaylistViewModel
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import com.google.firebase.auth.FirebaseAuth
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bumptech.glide.Glide // Asumiendo que usas Glide para cargar imágenes

@AndroidEntryPoint
class PlayListFrag : Fragment() {

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistId: String
    private lateinit var idUsuario: String

    private lateinit var cancionesAdapter: AdapterCanciones

    private val viewModel: PlaylistViewModel by lazy {
        ViewModelProvider(this).get(PlaylistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getString("playlistId") ?: ""
            idUsuario = FirebaseAuth.getInstance().uid ?: ""
        }

        if (playlistId.isEmpty()) {
            return
        }
        if (idUsuario.isEmpty()) {
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancionesAdapter = AdapterCanciones(
            onReproducirClick = { cancion ->
                reproducirCancion(cancion)
            },
            onFavoritoClick = { cancion, esFavoritoActual ->

            },
            onDescargarClick = { cancion ->

            },
            onAnadirClick = { cancion ->

            }, onEliminarClick = { cancion ->
                viewModel.eliminarCancionDePlaylist(playlistId, idUsuario, cancion.idCancion)
            },
            mostrarFavorito = false,
            mostrarDescargar = false,
            mostrarAnadir = false,
            mostrarEliminar = true
        )

        binding.rvPlaylistSongs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylistSongs.adapter = cancionesAdapter

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            if (playlist != null) {
                binding.tvPlaylistTitle.text = playlist.nombre

                if (playlist.canciones.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val canciones = withContext(Dispatchers.IO) {
                            viewModel.obtenerCancionPorId(playlist.canciones)
                        }
                        cancionesAdapter.actualizarDatos(canciones, emptySet())

                        if (canciones.isNotEmpty()) {
                            val randomSong = canciones.random()
                            Glide.with(requireContext())
                                .load(randomSong.urlImagen)
                                .placeholder(R.drawable.moha_beats_removebg_preview)
                                .error(R.drawable.moha_beats_removebg_preview)
                                .into(binding.ivPlaylistArtwork)
                        } else {
                            binding.ivPlaylistArtwork.setImageResource(R.drawable.moha_beats_removebg_preview)
                        }
                    }
                } else {
                    cancionesAdapter.actualizarDatos(emptyList(), emptySet())
                    binding.ivPlaylistArtwork.setImageResource(R.drawable.moha_beats_removebg_preview)
                }
            } else {

            }
        }

        if (playlistId.isNotEmpty() && idUsuario.isNotEmpty()) {
            viewModel.getPlaylist(playlistId, idUsuario)
        } else {
            return
        }

        binding.btnPlayMain.setOnClickListener {
            val todasLasCancionesDeLaPlaylist = cancionesAdapter.obtenerDatos()
            if (todasLasCancionesDeLaPlaylist.isNotEmpty()) {
                reproducirCancion(todasLasCancionesDeLaPlaylist[0])
            } else {

            }

            binding.btnPlayMain.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PLAYLIST_ID = "playlistId"
        fun newInstance(playlistId: String): PlayListFrag {
            val fragment = PlayListFrag()
            val bundle = Bundle()
            bundle.putString(ARG_PLAYLIST_ID, playlistId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCancionesParaReproducir = cancionesAdapter.obtenerDatos()
        val posicion = listaCancionesParaReproducir.indexOf(cancion).coerceAtLeast(0)

        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putParcelableArrayListExtra(MusicService.EXTRA_PLAYLIST, ArrayList(listaCancionesParaReproducir))
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
}