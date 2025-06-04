package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentPerfilBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth // Añadir para obtener el ID de usuario si no viene en argumentos

@AndroidEntryPoint
class PerfilFrag : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val perfilViewModel: PerfilViewModel by lazy {
        ViewModelProvider(this).get(PerfilViewModel::class.java)
    }

    private var currentUserId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("idUsuario")?.let { id ->
            currentUserId = id
            Log.d("PerfilFrag", "ID de usuario recibido en PerfilFrag: $currentUserId")
        } ?: run {
            currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            Log.e("PerfilFrag", "No se recibió 'idUsuario' en los argumentos. Intentando obtenerlo de FirebaseAuth: $currentUserId")
        }

        if (currentUserId.isBlank() || currentUserId == "invitado") {
            Toast.makeText(requireContext(), "Error: No se pudo obtener el ID del usuario. Los datos no se cargarán.", Toast.LENGTH_LONG).show()
            Log.e("PerfilFrag", "ERROR: currentUserId es nulo, vacío o 'invitado'.")
            binding.scrollPerfil.visibility = View.GONE
            binding.btnEditar.visibility = View.GONE
            binding.switchEditar.isEnabled = false
            return
        }

        perfilViewModel.usuario.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                binding.edtNombre.setText(it.nombreCompleto)
                binding.edtCorreo.setText(it.correo)
                binding.edtFecha.setText(it.fechaNacimiento)
                binding.edtTelefono.setText(it.telefono)
                binding.edtUsuario.setText(it.usuario)
            } ?: run {
                Log.w("PerfilFrag", "Usuario con ID '$currentUserId' no encontrado en la base de datos.")
                Toast.makeText(requireContext(), "No se pudieron cargar los datos del usuario. ID: $currentUserId", Toast.LENGTH_LONG).show()
                setEditMode(false)
                binding.switchEditar.isChecked = false
                binding.switchEditar.isEnabled = false
                binding.btnEditar.visibility = View.GONE
            }
        }

        perfilViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnEditar.isEnabled = !isLoading
            binding.switchEditar.isEnabled = !isLoading
            // Puedes añadir aquí la visibilidad de un ProgressBar si tienes uno
        }

        perfilViewModel.saveSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Perfil actualizado con éxito.", Toast.LENGTH_SHORT).show()
                binding.switchEditar.isChecked = false
            }
        }

        perfilViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        binding.switchEditar.setOnCheckedChangeListener { _, isChecked ->
            setEditMode(isChecked)
            binding.btnEditar.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        setEditMode(false)
        binding.btnEditar.visibility = View.GONE

        perfilViewModel.cargarUsuario(currentUserId)

        binding.btnEditar.setOnClickListener {
            val currentUsuario = perfilViewModel.usuario.value
            currentUsuario?.let { user ->
                val newNombreCompleto = binding.edtNombre.text.toString()
                val newCorreo = binding.edtCorreo.text.toString()
                val newFechaNacimiento = binding.edtFecha.text.toString()
                val newTelefono = binding.edtTelefono.text.toString()
                val newUsuarioDisplayName = binding.edtUsuario.text.toString()
                val newPassword: String? = null // Asume que no hay campo de contraseña para editar.
                // Si lo añades, léelo de tu TextInputEditText de contraseña.

                perfilViewModel.guardarCambiosPerfil(
                    newEmail = newCorreo,
                    newPassword = newPassword,
                    newNombreCompleto = newNombreCompleto,
                    newFechaNacimiento = newFechaNacimiento,
                    newUsuarioDisplayName = newUsuarioDisplayName,
                    newTelefono = newTelefono
                )
            } ?: run {
                Toast.makeText(requireContext(), "No hay datos de usuario para guardar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEditMode(isEditable: Boolean) {
        val editTexts = listOf<TextInputEditText>(
            binding.edtNombre,
            binding.edtCorreo,
            binding.edtFecha,
            binding.edtTelefono,
            binding.edtUsuario
        )

        editTexts.forEach { editText ->
            editText.isEnabled = isEditable
            editText.isFocusable = isEditable
            editText.isFocusableInTouchMode = isEditable
            editText.isClickable = isEditable
            editText.isLongClickable = isEditable
            editText.setCursorVisible(isEditable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}