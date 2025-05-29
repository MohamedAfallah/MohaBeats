package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityCrearCuentaBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.UsuarioViewModel

@AndroidEntryPoint
class CrearCuenta : AppCompatActivity() {

    private lateinit var binding: ActivityCrearCuentaBinding
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste de padding por sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Observamos creación correcta o error
        usuarioViewModel.idCreado.observe(this) { idCreado ->
            Toast.makeText(this, "Usuario creado con ID: $idCreado", Toast.LENGTH_LONG).show()
            // Ir a MainActivity después de crear la cuenta
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        usuarioViewModel.error.observe(this) { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        }

        binding.btnCrearCuenta.setOnClickListener {
            crearUsuarioDesdeFormulario()
        }
    }

    private fun crearUsuarioDesdeFormulario() {
        val nombre = binding.txtNombreCompleto.text.toString().trim()
        val fechaNac = binding.txtFechaNacimiento.text.toString().trim()
        val correo = binding.txtCorreo.text.toString().trim()
        val usuario = binding.txtUsuario.text.toString().trim()
        val telefono = binding.txtTelefono.text.toString().trim()
        val contrasena = binding.txtContrasena.text.toString()
        val confirmarContrasena = binding.txtConfirmarContrasena.text.toString()

        // Validaciones básicas
        if (nombre.isEmpty() || fechaNac.isEmpty() || correo.isEmpty() || usuario.isEmpty() ||
            telefono.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != confirmarContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el objeto Usuario (ajusta según tu modelo)
        val nuevoUsuario = Usuario(
            id = "", // o null, si se genera en el repositorio
            nombreCompleto = nombre,
            fechaNacimiento = fechaNac,
            correo = correo,
            usuario = usuario,
            telefono = telefono,
            contrasena = contrasena
        )

        // Llamar al ViewModel para crear el usuario
        usuarioViewModel.crearUsuario(nuevoUsuario)
    }
}