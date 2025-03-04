package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentFavoritosBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.recyclerView.Adapter

class FavoritosFrag : Fragment() {
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        val canciones = listOf(
            Musica("Blinding Lights", "The Weeknd", "https://example.com/blinding_lights.mp3"),
            Musica("Shape of You", "Ed Sheeran", "https://example.com/shape_of_you.mp3"),
        )

        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavoritos.adapter = Adapter(canciones)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}