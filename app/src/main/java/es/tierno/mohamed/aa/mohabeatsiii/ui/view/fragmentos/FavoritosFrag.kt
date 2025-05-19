package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentFavoritosBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones.Adapter
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.FavoritosViewModel

@AndroidEntryPoint
class FavoritosFrag : Fragment() {
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    private val favoritosViewModel : FavoritosViewModel
        get() = ViewModelProvider(this).get(FavoritosViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)

        val usuarioId = arguments?.getInt("usuarioId", -1) ?: -1

        favoritosViewModel.musicaFavorita.observe(viewLifecycleOwner, Observer { canciones ->
            canciones?.let {
                initRecyclerView(it)
            }
        })

        favoritosViewModel.onCreate(usuarioId)

        return binding.root
    }

    private fun initRecyclerView(canciones : List<Musica>) {
        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavoritos.adapter = Adapter(canciones)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}