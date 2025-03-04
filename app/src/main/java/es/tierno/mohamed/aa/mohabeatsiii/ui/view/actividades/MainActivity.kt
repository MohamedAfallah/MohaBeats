package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityMainBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.TiempoViewModel
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.UsuarioViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val tiempoViewModel: TiempoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    var usuarios: List<Usuario>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioViewModel.onCreate()
        tiempoViewModel.onCreate()

        usuarioViewModel.usuarios.observe(this, Observer { usuariosList ->
            usuarios = usuariosList
        })

        tiempoViewModel.tiempo.observe(this, Observer {
            val resultado = tiempoViewModel.tiempo.value?.tiempoHoy.toString()
            binding.lblTiempo.text = "Hoy Hace un Día\n" + dividirString(resultado, "=", ".")
        })

        binding.btnIniciar.setOnClickListener {
            if (usuarios != null && verificarInicio(binding.txtUsuario.text.toString(), binding.txtContrasena.text.toString())) {
                startActivity(Intent(this, PaginaInicial::class.java))
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun dividirString(input: String, inicio: String, fin: String): String? {
        val startIndex = input.indexOf(inicio)
        val endIndex = input.indexOf(fin)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + inicio.length, endIndex).trim()
        } else {
            null
        }
    }

    fun verificarInicio(usuarioIngresado: String, contrasenaIngresada: String): Boolean {
        if (usuarios.isNullOrEmpty()) {
            return false
        }

        for (usuario in usuarios!!) {
            if (usuario.usuario == usuarioIngresado && usuario.contrasena == contrasenaIngresada) {
                return true
            }
        }

        return false
    }
}
