package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Listener para botón iniciar sesión
        binding.btnIniciar.setOnClickListener {
            val email = binding.txtUsuario.text.toString().trim()
            val password = binding.txtContrasena.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.lblDenegado.text = "Por favor, ingrese correo y contraseña."
                return@setOnClickListener
            }

            // Intentar iniciar sesión con Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Inicio sesión correcto
                        val user = auth.currentUser
                        irAPaginaInicial(user?.uid)
                    } else {
                        // Error de autenticación
                        binding.lblDenegado.text = "Usuario o contraseña incorrectos."
                    }
                }
        }

        binding.btnInvitado.setOnClickListener{
            irAPaginaInicial("invitado")
        }

        // Registro
        binding.lblRegistrarse.setOnClickListener {
            accederCreacionCuenta()
        }
    }

    private fun irAPaginaInicial(user: String?) {
        val intent = Intent(this, PaginaInicial::class.java)
        intent.putExtra("usuarioId", user)
        startActivity(intent)
        finish()
    }

    private fun accederCreacionCuenta() {
        val intent = Intent(this, CrearCuenta::class.java)
        startActivity(intent)
    }
}
