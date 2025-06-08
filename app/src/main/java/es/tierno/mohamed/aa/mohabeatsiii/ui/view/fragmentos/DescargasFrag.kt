package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentDescargasHistorialBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica // Asegúrate de que Musica esté importada
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService // Importa MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones_descargadas.CancionesDescargadasAdapter
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.DescargasViewModel

@AndroidEntryPoint
class DescargasFrag : Fragment() {

    private var _binding: FragmentDescargasHistorialBinding? = null
    private val binding get() = _binding!!

    private val descargasViewModel: DescargasViewModel by viewModels()

    private lateinit var cancionesAdapter: CancionesDescargadasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescargasHistorialBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.txtTitulo.text = getString(R.string.txtDescargas)

        setupRecyclerView()
        observeViewModel()

        return view
    }

    private fun setupRecyclerView() {
        cancionesAdapter = CancionesDescargadasAdapter(
            onItemClick = { musica ->
                reproducirCancion(musica)
            },
            onDeleteClick = { musica ->
                musica.idCancion?.let { songDbId ->
                    descargasViewModel.eliminarCancion(songDbId)
                } ?: run {
                }
            }
        )

        binding.rvCanciones.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cancionesAdapter
        }
    }

    private fun observeViewModel() {
        descargasViewModel.cancionesDescargadas.observe(viewLifecycleOwner) { canciones ->
            cancionesAdapter.submitList(canciones)
        }
    }

    private fun reproducirCancion(cancion: Musica) {
        val listaCanciones = cancionesAdapter.currentList
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
                .replace(R.id.reproductorContainer, reproductorFrag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}