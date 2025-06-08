package es.tierno.mohamed.aa.mohabeatsiii.ui.view.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

import es.tierno.mohamed.aa.mohabeatsiii.databinding.BottomSheetPlaylistBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist.AdapterPlaylist
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.vm_bottom_sheet.BtmShtPlaylistViewModel
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views.DialogCrearPlaylist // Importa tu nueva clase de diálogo

@AndroidEntryPoint
class BottomSheetPlaylist : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BtmShtPlaylistViewModel
    private lateinit var adaptadorPlaylists: AdapterPlaylist

    private var cancionAAnadir: Musica? = null
    private lateinit var idUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cancionAAnadir = it.getParcelable(ARG_CANCION_A_ANADIR)
        }
        idUsuario = FirebaseAuth.getInstance().uid ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = 400.dpToPx()
            bottomSheet?.requestLayout()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BtmShtPlaylistViewModel::class.java)

        configurarRecyclerView()
        observarViewModel()

        if (idUsuario.isNotEmpty()) {
            viewModel.cargarPlaylists(idUsuario)
        } else {
            Toast.makeText(requireContext(), "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.cardNuevaPlaylist.setOnClickListener {
            mostrarDialogoCrearPlaylist()
        }
    }

    private fun configurarRecyclerView() {
        adaptadorPlaylists = AdapterPlaylist(
            emptyList(),
            { playlist ->
                cancionAAnadir?.let { cancion ->
                    viewModel.anadirCancionAPlaylist(playlist.id, idUsuario, cancion.idCancion)
                } ?: run {
                    Toast.makeText(requireContext(), "Error: No hay canción para añadir.", Toast.LENGTH_SHORT).show()
                }
            },
            onEliminarClick = null,
            onAgregarPostClick = null,
            showEliminarButton = false,
            showAgregarPostButton = false
        )
        binding.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylists.adapter = adaptadorPlaylists
    }

    private fun observarViewModel() {
        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            adaptadorPlaylists.actualizarListas(playlists)
        }

        viewModel.resultadoAnadirCancion.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Canción añadida a la playlist.", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Error al añadir canción a la playlist.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.resultadoCreacionYAnadido.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Playlist creada y canción añadida.", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Error al crear/añadir a la playlist.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoCrearPlaylist() {
        val dialog = DialogCrearPlaylist { playlistName ->
            if (idUsuario.isNotEmpty() && cancionAAnadir != null) {
                viewModel.crearNuevaPlaylistYAnadirCancion(idUsuario, playlistName, cancionAAnadir!!.idCancion)
            } else {
                Toast.makeText(requireContext(), "Error: Datos incompletos para crear playlist.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show(childFragmentManager, DialogCrearPlaylist.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetPlaylist"
        private const val ARG_CANCION_A_ANADIR = "cancion_a_anadir"

        fun newInstance(cancion: Musica): BottomSheetPlaylist {
            val fragment = BottomSheetPlaylist()
            val args = Bundle().apply {
                putParcelable(ARG_CANCION_A_ANADIR, cancion)
            }
            fragment.arguments = args
            return fragment
        }
    }

    fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale).toInt()
    }
}