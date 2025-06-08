package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentDescargasHistorialBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.PaginaInicial
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.AdapterCanciones
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.HistorialViewModel

@AndroidEntryPoint
class HistorialFrag : Fragment() {

    private var _binding: FragmentDescargasHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterCanciones
    private val viewModel: HistorialViewModel by viewModels()

    private var idUsuario: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescargasHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUsuario = arguments?.getString("idUsuario") ?: ""

        adapter = AdapterCanciones(
            onReproducirClick = { cancion -> reproducirCancion(cancion) },
            onFavoritoClick = { cancion, esFavoritoActual ->
                //Vacio porque no vamos a hacer ninguna funcion aqui
            },
            onDescargarClick = { cancion ->
                //Vacio por la misma razon que el de arriba
            },
            onAnadirClick = { cancion ->

            }, onEliminarClick = { cancion ->

            },
            mostrarFavorito = false,
            mostrarDescargar = false,
            mostrarAnadir = false,
            mostrarEliminar = false
        )

        binding.rvCanciones.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCanciones.adapter = adapter

        viewModel.musicaHistorial.observe(viewLifecycleOwner, Observer { listaCancionesHistorial ->
            adapter.actualizarDatos(listaCancionesHistorial, emptySet())
        })

        if (idUsuario.isNotEmpty()) {
            viewModel.onCreate(idUsuario)
        } else {
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