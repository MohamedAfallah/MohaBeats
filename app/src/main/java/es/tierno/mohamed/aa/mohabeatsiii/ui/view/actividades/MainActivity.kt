package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityMainBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.UsuarioViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    var usuarios: List<Usuario>? = null
    var usuarioIniciado : Usuario? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioViewModel.onCreate()
        usuarioViewModel.usuarios.observe(this, Observer { usuariosList ->
            usuarios = usuariosList
        })

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso ya concedido", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
        }

        binding.btnIniciar.setOnClickListener {
            val intento = Intent(this, PaginaInicial::class.java)
            startActivity(intento)

            if (usuarios != null && verificarInicio(binding.txtUsuario.text.toString(), binding.txtContrasena.text.toString())) {
                val intent = Intent(this, PaginaInicial::class.java)
                //Pasar el id del usuario iniciado para poder recuperar sus peliculas favoritas.
                intent.putExtra("usuarioId", usuarioIniciado?.id)
                startActivity(intent)
            } else {
                binding.lblDenegado.text = "Usuario o contraseña incorrectos."
            }
        }

        binding.lblRegistrarse.setOnClickListener {
            val intent = Intent(this, CrearCuenta::class.java)
            startActivity(intent)
        }
    }

    //Metodo para dividir el string que devuelve la API
    fun dividirString(input: String, inicio: String, fin: String): String? {
        val startIndex = input.indexOf(inicio)
        val endIndex = input.indexOf(fin)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + inicio.length, endIndex).trim()
        } else {
            null
        }
    }

    //Metodo para verificar el inicio de sesion de un usuario
    fun verificarInicio(usuarioIngresado: String, contrasenaIngresada: String): Boolean {
        if (usuarios.isNullOrEmpty()) {
            return false
        }

        for (usuario in usuarios!!) {
            if (usuario.usuario == usuarioIngresado && usuario.contrasena == contrasenaIngresada) {
                usuarioIniciado = usuario
                return true
            }
        }

        return false
    }

    fun accederCreacionCuenta(){

    }
}
