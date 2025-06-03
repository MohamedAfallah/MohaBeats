package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentFavoritosBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones

class FavoritosFrag : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterCanciones
    private var favoritos: List<Musica> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la lista de favoritos (puedes pasarla con argumentos o cargar aquí)
        favoritos = arguments?.getParcelableArrayList("favoritos") ?: emptyList()

        adapter = AdapterCanciones { cancion -> reproducirCancion(cancion) }

        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavoritos.adapter = adapter

        adapter.actualizarDatos(favoritos)
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCanciones = adapter.obtenerDatos()
        val posicion = listaCanciones.indexOf(cancion).coerceAtLeast(0)

        // Iniciar el servicio con lista completa y posición de la canción
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putParcelableArrayListExtra(MusicService.EXTRA_PLAYLIST, ArrayList(listaCanciones))
            putExtra("EXTRA_START_INDEX", posicion)
        }
        requireContext().startService(intent)

        // Ocultar el reproductor mini y mostrar el reproductor normal
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



