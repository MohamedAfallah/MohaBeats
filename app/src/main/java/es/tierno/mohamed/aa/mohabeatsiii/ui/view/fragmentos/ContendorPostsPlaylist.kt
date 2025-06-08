package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentContendorPostsPlaylistBinding

class ContendorPostsPlaylist : Fragment() {

    private var _binding: FragmentContendorPostsPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContendorPostsPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabMisPlaylists.setOnClickListener {
            seleccionarPestana(Pestana.PLAYLISTS)
        }

        binding.tabPosts.setOnClickListener {
            seleccionarPestana(Pestana.POSTS)
        }

        if (savedInstanceState == null) {
            seleccionarPestana(Pestana.PLAYLISTS)
        }
    }

    private fun seleccionarPestana(pestana: Pestana) {
        binding.underlineMisPlaylists.visibility = View.INVISIBLE
        binding.underlinePosts.visibility = View.INVISIBLE

        binding.tvMisPlaylists.setTextColor(resources.getColor(R.color.blancoAgradables, null))
        binding.tvPosts.setTextColor(resources.getColor(R.color.blancoAgradables, null))

        val fragmentoCargar: Fragment

        when (pestana) {
            Pestana.PLAYLISTS -> {
                binding.underlineMisPlaylists.visibility = View.VISIBLE
                binding.tvMisPlaylists.setTextColor(resources.getColor(R.color.colorDetalles, null))
                fragmentoCargar = ContendorPlaylists()
            }
            Pestana.POSTS -> {
                binding.underlinePosts.visibility = View.VISIBLE
                binding.tvPosts.setTextColor(resources.getColor(R.color.colorDetalles, null))
                fragmentoCargar = PostsFrag()
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, fragmentoCargar)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private enum class Pestana {
        PLAYLISTS, POSTS
    }
}