package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityCrearCuentaBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.CrearCuentaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

@AndroidEntryPoint
class CrearCuenta : AppCompatActivity() {

    private lateinit var binding: ActivityCrearCuentaBinding
    private var dialogoCarga: Dialog? = null
    private var textoMensaje: TextView? = null
    private var animacionLottie: LottieAnimationView? = null

    private val viewModel: CrearCuentaViewModel by viewModels()

    private val manejador = Handler(Looper.getMainLooper())
    private val intervaloVerificacion = 3000L

    private val tareaVerificarCorreo = object : Runnable {
        override fun run() {
            viewModel.checkEmailVerified()
            manejador.postDelayed(this, intervaloVerificacion)
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

        configurarValidacionEntrada()
        configurarSelectorFecha()

        binding.btnCrearCuenta.setOnClickListener {
            crearUsuarioDesdeFormulario()
        }

        observarViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        manejador.removeCallbacks(tareaVerificarCorreo)
    }

    private fun configurarValidacionEntrada() {
        val textWatcherGenerico = { layout: TextInputLayout ->
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    validarCampoRequerido(layout, s.isNullOrEmpty())
                }
                override fun afterTextChanged(s: Editable?) {}
            }
        }

        binding.txtNombreCompleto.addTextChangedListener(textWatcherGenerico(binding.layoutNombreCompleto))
        binding.txtUsuario.addTextChangedListener(textWatcherGenerico(binding.layoutUsuario))

        binding.txtTelefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarTelefono(binding.layoutTelefono, s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.txtFechaNacimiento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarFechaNacimiento(binding.layoutFechaNacimiento, s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.txtCorreo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarCorreo(binding.layoutCorreo, s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.txtContrasena.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val contrasena = s.toString()
                validarContrasena(binding.layoutContrasena, contrasena)
                validarConfirmarContrasena(binding.txtContrasena.text.toString(), binding.txtConfirmarContrasena.text.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.txtConfirmarContrasena.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarConfirmarContrasena(binding.txtContrasena.text.toString(), s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun configurarSelectorFecha() {
        binding.txtFechaNacimiento.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val selectorFechaDialogo = DatePickerDialog(
                this,
                R.style.DatePickerDialogTheme,
                { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", diaSeleccionado, mesSeleccionado + 1, anioSeleccionado)
                    binding.txtFechaNacimiento.setText(fechaSeleccionada)
                    limpiarErrorCampo(binding.layoutFechaNacimiento)
                },
                anio, mes, dia
            )

            val maxCalendar = Calendar.getInstance()
            maxCalendar.set(2010, Calendar.DECEMBER, 31)
            selectorFechaDialogo.datePicker.maxDate = maxCalendar.timeInMillis

            selectorFechaDialogo.show()
        }
    }

    private fun validarCampoRequerido(textInputLayout: TextInputLayout, estaVacio: Boolean): Boolean {
        return if (estaVacio) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_campo_requerido, textInputLayout.hint))
            false
        } else {
            limpiarErrorCampo(textInputLayout)
            true
        }
    }

    private fun validarCorreo(textInputLayout: TextInputLayout, correo: String): Boolean {
        if (correo.isEmpty()) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_correo_requerido))
            return false
        }
        val patronCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return if (!Pattern.matches(patronCorreo, correo)) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_correo_invalido))
            false
        } else {
            limpiarErrorCampo(textInputLayout)
            true
        }
    }

    private fun validarContrasena(textInputLayout: TextInputLayout, contrasena: String): Boolean {
        if (contrasena.isEmpty()) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_contrasena_requerida))
            return false
        } else if (contrasena.length < 6) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_contrasena_corta))
            return false
        } else {
            limpiarErrorCampo(textInputLayout)
            return true
        }
    }

    private fun validarConfirmarContrasena(contrasena: String, confirmarContrasena: String): Boolean {
        if (confirmarContrasena.isEmpty()) {
            establecerErrorCampo(binding.layoutConfirmarContrasena, getString(R.string.error_confirmar_contrasena_requerida))
            return false
        } else if (contrasena != confirmarContrasena) {
            establecerErrorCampo(binding.layoutConfirmarContrasena, getString(R.string.error_contrasenas_no_coinciden))
            return false
        } else {
            limpiarErrorCampo(binding.layoutConfirmarContrasena)
            return true
        }
    }

    private fun validarTelefono(textInputLayout: TextInputLayout, telefono: String): Boolean {
        return if (telefono.length == 9 && telefono.all { it.isDigit() }) {
            limpiarErrorCampo(textInputLayout)
            true
        } else {
            establecerErrorCampo(textInputLayout, getString(R.string.error_telefono_invalido))
            false
        }
    }

    private fun validarFechaNacimiento(textInputLayout: TextInputLayout, fecha: String): Boolean {
        if (fecha.isEmpty()) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_campo_requerido, textInputLayout.hint))
            return false
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val selectedDate = sdf.parse(fecha)
            val cal = Calendar.getInstance()
            cal.time = selectedDate ?: return false
            val year = cal.get(Calendar.YEAR)

            if (year <= 2010) {
                limpiarErrorCampo(textInputLayout)
                true
            } else {
                establecerErrorCampo(textInputLayout, getString(R.string.error_fecha_nacimiento_invalida))
                false
            }
        } catch (e: ParseException) {
            establecerErrorCampo(textInputLayout, getString(R.string.error_fecha_nacimiento_invalida))
            false
        }
    }

    private fun establecerErrorCampo(textInputLayout: TextInputLayout, mensajeError: String) {
        textInputLayout.error = mensajeError
        textInputLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.rojo)
        textInputLayout.hintTextColor = ContextCompat.getColorStateList(this, R.color.rojo)
    }

    private fun limpiarErrorCampo(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
        textInputLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.colorDetalles)
        textInputLayout.hintTextColor = ContextCompat.getColorStateList(this, R.color.colorDetalles)
    }

    private fun mostrarDialogoCarga() {
        if (dialogoCarga == null) {
            dialogoCarga = Dialog(this).apply {
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
            textoMensaje = dialogoCarga?.findViewById(R.id.textoMensaje)
            animacionLottie = dialogoCarga?.findViewById(R.id.lottieAnimation)
        }
        textoMensaje?.text = getString(R.string.loading_crear)
        animacionLottie?.apply {
            setAnimation("loading_animation.json")
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
        dialogoCarga?.show()
    }

    private fun ocultarDialogoCarga() {
        dialogoCarga?.dismiss()
    }

    private fun crearUsuarioDesdeFormulario() {
        val nombre = binding.txtNombreCompleto.text.toString().trim()
        val fechaNac = binding.txtFechaNacimiento.text.toString().trim()
        val correo = binding.txtCorreo.text.toString().trim()
        val usuario = binding.txtUsuario.text.toString().trim()
        val telefono = binding.txtTelefono.text.toString().trim()
        val contrasena = binding.txtContrasena.text.toString()
        val confirmarContrasena = binding.txtConfirmarContrasena.text.toString()

        var todosLosCamposValidos = true

        todosLosCamposValidos = validarCampoRequerido(binding.layoutNombreCompleto, nombre.isEmpty()) && todosLosCamposValidos
        todosLosCamposValidos = validarFechaNacimiento(binding.layoutFechaNacimiento, fechaNac) && todosLosCamposValidos
        todosLosCamposValidos = validarCorreo(binding.layoutCorreo, correo) && todosLosCamposValidos
        todosLosCamposValidos = validarCampoRequerido(binding.layoutUsuario, usuario.isEmpty()) && todosLosCamposValidos
        todosLosCamposValidos = validarTelefono(binding.layoutTelefono, telefono) && todosLosCamposValidos
        todosLosCamposValidos = validarContrasena(binding.layoutContrasena, contrasena) && todosLosCamposValidos
        todosLosCamposValidos = validarConfirmarContrasena(contrasena, confirmarContrasena) && todosLosCamposValidos

        if (!todosLosCamposValidos) {
            Toast.makeText(this, getString(R.string.error_corregir_formulario), Toast.LENGTH_SHORT).show()
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

        mostrarDialogoCarga()
        viewModel.crearUsuario(nuevoUsuario)
    }

    private fun observarViewModel() {
        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMsg ->
                errorMsg?.let {
                    ocultarDialogoCarga()
                    manejador.removeCallbacks(tareaVerificarCorreo)
                    Toast.makeText(this@CrearCuenta, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.idCreado.collectLatest { id ->
                id?.let {
                    textoMensaje?.text = getString(R.string.loading_enviando_verificacion)
                    animacionLottie?.setAnimation("email_sending.json")
                    animacionLottie?.playAnimation()
                    manejador.postDelayed(tareaVerificarCorreo, intervaloVerificacion)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.emailVerificado.collectLatest { verificado ->
                if (verificado) {
                    textoMensaje?.text = getString(R.string.success_correo_verificado)
                    animacionLottie?.apply {
                        setAnimation("verificacion_exitosa.json")
                        repeatCount = 0
                        playAnimation()

                        Handler(Looper.getMainLooper()).postDelayed({
                            ocultarDialogoCarga()
                            val intent = Intent(this@CrearCuenta, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, duration)
                    }
                    manejador.removeCallbacks(tareaVerificarCorreo)

                } else {
                    textoMensaje?.text = getString(R.string.loading_esperando_verificacion)
                }
            }
        }
    }
}