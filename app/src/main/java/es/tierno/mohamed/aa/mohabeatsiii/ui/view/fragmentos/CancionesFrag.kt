package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentCancionesBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.recyclerView.Adapter
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.MusicaViewModel

@AndroidEntryPoint
class CancionesFrag : Fragment() {
    private var _binding: FragmentCancionesBinding? = null
    private val binding get() = _binding!!

    private val musicaViewModel: MusicaViewModel
        get() = ViewModelProvider(this).get(MusicaViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCancionesBinding.inflate(inflater, container, false)

        //Observar el ViewModel
        musicaViewModel.musica.observe(viewLifecycleOwner, Observer { canciones ->
            canciones?.let {
                initRecyclerView(it)
            }
        })

        musicaViewModel.onCreate()



        return binding.root
    }

    //Iniciar el recyclerView de canciones
    private fun initRecyclerView(canciones : List<Musica>) {
        binding.recyclerViewCanciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCanciones.adapter = Adapter(canciones)
    }

    //Liberar la referencia del binding cuando se destruye el fragmento.wwww
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}