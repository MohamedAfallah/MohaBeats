package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

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
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.Adapter
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.MusicaViewModel

@AndroidEntryPoint
class CancionesFrag : Fragment() {

    private var _binding: FragmentCancionesBinding? = null
    private val binding get() = _binding!!

    private val musicaViewModel: MusicaViewModel
        get() = ViewModelProvider(this).get(MusicaViewModel::class.java)

    private var alturaInicialRecyclerView = 0
    private var alturaMaximaRecyclerView = 0
    private var acumuladoScroll = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCancionesBinding.inflate(inflater, container, false)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.fragExContainer.id, ExplorarFrag())
                .commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener altura inicial y máxima cuando el layout ya esté medido
        binding.recyclerViewCanciones.post {
            alturaInicialRecyclerView = binding.recyclerViewCanciones.height
            alturaMaximaRecyclerView = binding.fragExContainer.height
        }

        // Configurar RecyclerView y observar ViewModel
        musicaViewModel.musica.observe(viewLifecycleOwner) { canciones ->
            canciones?.let { initRecyclerView(it) }
        }
        musicaViewModel.onCreate()

        binding.recyclerViewCanciones.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy == 0 || alturaMaximaRecyclerView == 0) return

                acumuladoScroll += dy
                if (acumuladoScroll < 0) acumuladoScroll = 0

                // Nueva altura para RecyclerView, máximo fragExContainer
                val nuevaAltura = (alturaInicialRecyclerView + acumuladoScroll)
                    .coerceAtMost(alturaMaximaRecyclerView)

                val recyclerParams = binding.recyclerViewCanciones.layoutParams
                recyclerParams.height = nuevaAltura
                binding.recyclerViewCanciones.layoutParams = recyclerParams

                // Alfa para fragExContainer (desvanecer)
                val porcentaje =
                    acumuladoScroll.toFloat() / (alturaMaximaRecyclerView - alturaInicialRecyclerView)
                val alpha = (1f - porcentaje).coerceAtLeast(0f)
                binding.fragExContainer.alpha = alpha

                // Calcular cuánto deben subir textoCanciones y view (hasta top)
                val desplazamientoMaximo = binding.textoCanciones.top.toFloat()
                val desplazamientoActual = (porcentaje * desplazamientoMaximo).coerceAtMost(desplazamientoMaximo)

                binding.textoCanciones.translationY = -desplazamientoActual
                binding.view.translationY = -desplazamientoActual
            }
        })
    }

    private fun initRecyclerView(canciones: List<Musica>) {
        binding.recyclerViewCanciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCanciones.adapter = Adapter(canciones)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

