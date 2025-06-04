package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityCrearCuentaBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.CrearCuentaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrearCuenta : AppCompatActivity() {

    private lateinit var binding: ActivityCrearCuentaBinding
    private var dialogLoading: Dialog? = null
    private var textoMensaje: TextView? = null
    private var lottieAnimation: LottieAnimationView? = null

    private val viewModel: CrearCuentaViewModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 3000L // 3 segundos

    private val checkEmailVerificationRunnable = object : Runnable {
        override fun run() {
            viewModel.checkEmailVerified()
            handler.postDelayed(this, checkInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCrearCuenta.setOnClickListener {
            crearUsuarioDesdeFormulario()
        }

        observarViewModel()
    }

    private fun mostrarDialogoLoading() {
        if (dialogLoading == null) {
            dialogLoading = Dialog(this).apply {
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
            // Obtener referencias a las vistas del diálogo para manipularlas
            textoMensaje = dialogLoading?.findViewById(R.id.textoMensaje)
            lottieAnimation = dialogLoading?.findViewById(R.id.lottieAnimation)
        }
        textoMensaje?.text = getString(R.string.loading_crear) // Texto inicial
        lottieAnimation?.apply {
            setAnimation("loading_animation.json")
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
        dialogLoading?.show()
    }

    private fun ocultarDialogoLoading() {
        dialogLoading?.dismiss()
    }

    private fun crearUsuarioDesdeFormulario() {
        val nombre = binding.txtNombreCompleto.text.toString().trim()
        val fechaNac = binding.txtFechaNacimiento.text.toString().trim()
        val correo = binding.txtCorreo.text.toString().trim()
        val usuario = binding.txtUsuario.text.toString().trim()
        val telefono = binding.txtTelefono.text.toString().trim()
        val contrasena = binding.txtContrasena.text.toString()
        val confirmarContrasena = binding.txtConfirmarContrasena.text.toString()

        if (nombre.isEmpty() || fechaNac.isEmpty() || correo.isEmpty() || usuario.isEmpty() ||
            telefono.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != confirmarContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsuario = Usuario(
            id = "",
            nombreCompleto = nombre,
            fechaNacimiento = fechaNac,
            correo = correo,
            usuario = usuario,
            telefono = telefono,
            contrasena = contrasena
        )

        mostrarDialogoLoading()
        viewModel.crearUsuario(nuevoUsuario)
    }

    private fun observarViewModel() {
        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMsg ->
                errorMsg?.let {
                    ocultarDialogoLoading()
                    handler.removeCallbacks(checkEmailVerificationRunnable)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.idCreado.collectLatest { id ->
                id?.let {
                    handler.postDelayed(checkEmailVerificationRunnable, checkInterval)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.emailVerificado.collectLatest { verificado ->
                if (verificado) {
                    textoMensaje?.text = "Correo verificado con éxito"
                    lottieAnimation?.apply {
                        setAnimation("verificacion_exitosa.json")
                        repeatCount = 0 // No repetir
                        playAnimation()
                    }
                    handler.removeCallbacks(checkEmailVerificationRunnable)

                    Handler(Looper.getMainLooper()).postDelayed({
                        ocultarDialogoLoading()
                        Toast.makeText(this@CrearCuenta, "Iniciando sesión...", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@CrearCuenta, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkEmailVerificationRunnable)
    }
}

