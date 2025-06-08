package es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.DialogCrearPostBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.vm_dialog.dialogCrearPostViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogCrearPost : DialogFragment() {

    private var _binding: DialogCrearPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: dialogCrearPostViewModel by viewModels()

    companion object {
        private const val ARG_PLAYLIST_ID = "playlist_id"

        fun newInstance(playlistId: String): DialogCrearPost {
            val fragment = DialogCrearPost()
            val args = Bundle()
            args.putString(ARG_PLAYLIST_ID, playlistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCrearPostBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getString(ARG_PLAYLIST_ID)
            ?: throw IllegalArgumentException("DialogCrearPost requiere un playlistId.")

        val userId = FirebaseAuth.getInstance().uid

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Error: Usuario no autenticado para crear post.", Toast.LENGTH_SHORT).show()
            dismiss()
            return
        }

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnPublicar.setOnClickListener {
            val comentario = binding.etComentarioPost.text.toString().trim()
            if (comentario.isNotEmpty()) {
                viewModel.createPost(userId, comentario, playlistId)
            } else {
                binding.etComentarioPost.error = "El comentario no puede estar vacío"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postCreationStatus.collectLatest { success ->
                success?.let {
                    if (it) {
                        Toast.makeText(requireContext(), "Post creado exitosamente.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), viewModel.error.value ?: "Error al crear el post.", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.resetStatus()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}