package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentContendorPlaylistsBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist.AdapterPlaylist
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views.DialogCrearPost
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.ContenedorPlayListViewModel

@AndroidEntryPoint
class ContendorPlaylists : Fragment() {

    private var _binding: FragmentContendorPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistsAdapter: AdapterPlaylist
    private lateinit var idUsuario: String

    private val viewModel: ContenedorPlayListViewModel by lazy {
        ViewModelProvider(this).get(ContenedorPlayListViewModel::class.java)
    }

    companion object {
        private const val ARG_ID_USUARIO = "arg_id_usuario"
        fun nuevaInstancia(idUsuario: String): ContendorPlaylists {
            val fragmento = ContendorPlaylists()
            val bundle = Bundle()
            bundle.putString(ARG_ID_USUARIO, idUsuario)
            fragmento.arguments = bundle
            return fragmento
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContendorPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idUsuario = FirebaseAuth.getInstance().uid ?: ""

        playlistsAdapter = AdapterPlaylist(
            emptyList(),
            { playlist ->
                val fragment = PlayListFrag().apply {
                    arguments = Bundle().apply {
                        putString("playlistId", playlist.id)
                    }
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onEliminarClick = { playlist ->
                if (idUsuario.isNotEmpty()) {
                    viewModel.eliminarPlaylist(playlist.id, idUsuario)
                } else {
                    Toast.makeText(requireContext(), "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
                }
            },
            onAgregarPostClick = { playlist ->
                if (idUsuario.isNotEmpty()) {
                    val dialog = DialogCrearPost.newInstance(playlist.id)
                    dialog.show(childFragmentManager, "DialogCrearPost")
                } else {
                    Toast.makeText(requireContext(), "Inicia sesión para añadir posts.", Toast.LENGTH_SHORT).show()
                }
            },
            showEliminarButton = true,
            showAgregarPostButton = true
        )

        binding.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylists.adapter = playlistsAdapter

        viewModel.playlists.observe(viewLifecycleOwner) { listas ->
            playlistsAdapter.actualizarListas(listas)
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Playlist eliminada correctamente.", Toast.LENGTH_SHORT).show()
                if (idUsuario.isNotEmpty()) {
                    viewModel.getPlaylits(idUsuario)
                }
            } else {
                Toast.makeText(requireContext(), "Error al eliminar la playlist.", Toast.LENGTH_SHORT).show()
            }
        }

        if (idUsuario.isNotEmpty()) {
            viewModel.getPlaylits(idUsuario)
        } else {
            Toast.makeText(requireContext(), "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}