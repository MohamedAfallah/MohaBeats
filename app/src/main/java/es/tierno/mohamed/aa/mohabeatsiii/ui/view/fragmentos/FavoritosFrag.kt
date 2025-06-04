package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentFavoritosBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.FavoritosViewModel

@AndroidEntryPoint
class FavoritosFrag : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterCanciones
    private val viewModel: FavoritosViewModel by viewModels()

    private var idUsuario: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUsuario = arguments?.getString("idUsuario") ?: ""
        Log.d("MohabeatsDebug", "FavoritosFrag: ID de usuario recibido de los argumentos: $idUsuario")


        adapter = AdapterCanciones(
            idUsuario = idUsuario,
            onReproducirClick = { cancion -> reproducirCancion(cancion) },
            onFavoritoClick = { cancion, esFavoritoActual ->
                if (idUsuario.isEmpty() || idUsuario == "invitado") {
                    Log.d("MohabeatsDebug", "FavoritosFrag: Invitado intentó gestionar favoritos.")
                } else {
                    if (esFavoritoActual) {
                        Log.d("MohabeatsDebug", "FavoritosFrag: Eliminando canción ${cancion.idCancion} de favoritos.")
                        viewModel.eliminarDeFavoritos(idUsuario, cancion.idCancion)
                    } else {
                        Log.d("MohabeatsDebug", "FavoritosFrag: Añadiendo canción ${cancion.idCancion} a favoritos.")
                        viewModel.anadirAFavoritos(idUsuario, cancion.idCancion)
                    }
                }
            }
        )

        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavoritos.adapter = adapter

        viewModel.musicaFavorita.observe(viewLifecycleOwner, Observer { listaCancionesFavoritas ->
            Log.d("MohabeatsDebug", "FavoritosFrag: musicaFavorita LiveData actualizado con ${listaCancionesFavoritas.size} elementos.")
            val favoritasIdsSet = mutableSetOf<String>()
            listaCancionesFavoritas.forEach { musica ->
                favoritasIdsSet.add(musica.idCancion)
            }
            adapter.actualizarDatos(listaCancionesFavoritas, favoritasIdsSet)
        })

        if (idUsuario.isNotEmpty()) {
            Log.d("MohabeatsDebug", "FavoritosFrag: Llamando a viewModel.onCreate con idUsuario: $idUsuario")
            viewModel.onCreate(idUsuario)
        } else {
            Log.e("MohabeatsDebug", "FavoritosFrag: idUsuario es VACÍO. No se pueden obtener los favoritos.")
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