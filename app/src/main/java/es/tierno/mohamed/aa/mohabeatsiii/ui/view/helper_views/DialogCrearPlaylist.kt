package es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import es.tierno.mohamed.aa.mohabeatsiii.databinding.DialogAnadirPlaylistBinding

class DialogCrearPlaylist(
    private val onCreateClick: (String) -> Unit
) : DialogFragment() {

    private var _binding: DialogAnadirPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAnadirPlaylistBinding.inflate(inflater, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnCrear.setOnClickListener {
            val playlistName = binding.etNombrePlaylist.text.toString().trim()
            if (playlistName.isNotEmpty()) {
                onCreateClick(playlistName)
                dismiss()
            } else {
                binding.textInputLayoutNombre.error = "El nombre no puede estar vacío"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DialogCrearPlaylist"
    }
}