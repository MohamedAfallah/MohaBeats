package es.tierno.mohamed.aa.mohabeatsiii.ui.view.bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.tierno.mohamed.aa.mohabeatsiii.databinding.BottomSheetInvitadoBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.MainActivity
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades.CrearCuenta

class BottomSheetInvitado : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInvitadoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetInvitadoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnIniciarSesion.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            dismiss()
        }

        binding.btnCrearCuenta.setOnClickListener {
            val intent = Intent(requireContext(), CrearCuenta::class.java)
            startActivity(intent)
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}