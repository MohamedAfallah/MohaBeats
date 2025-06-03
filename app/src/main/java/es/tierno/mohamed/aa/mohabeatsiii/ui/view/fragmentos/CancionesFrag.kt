package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterCanciones { cancion -> reproducirCancion(cancion) }

        binding.recyclerViewCanciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCanciones.adapter = adapter

        musicaViewModel.musicaPaginada.observe(viewLifecycleOwner) { canciones ->
            adapter.actualizarDatos(canciones)
        }

        binding.recyclerViewCanciones.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 2 >= totalItemCount) {
                    musicaViewModel.cargarPagina()
                }
            }
        })

        musicaViewModel.onCreate()
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCanciones = adapter.obtenerDatos()
        val posicion = listaCanciones.indexOf(cancion).coerceAtLeast(0)

        // Iniciar el servicio
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putParcelableArrayListExtra(MusicService.EXTRA_PLAYLIST, ArrayList(listaCanciones))
            putExtra("EXTRA_START_INDEX", posicion)
        }
        requireContext().startService(intent)

        // Ocultar el reproductor mini desde la actividad padre
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
