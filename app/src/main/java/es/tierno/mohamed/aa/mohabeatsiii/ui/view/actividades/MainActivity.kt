package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.tierno.mohamed.aa.mohabeatsiii.R
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

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            irAPaginaInicial(currentUser.uid)
            finish()
            return
        }

        binding.btnIniciar.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtContrasena.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.lblDenegado.text = getString(R.string.error_credenciales_vacias) // Replaced hardcoded string
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        irAPaginaInicial(user?.uid)
                    } else {
                        binding.lblDenegado.text = getString(R.string.error_credenciales_incorrectas) // Replaced hardcoded string
                    }
                }
        }

        binding.btnInvitado.setOnClickListener{
            irAPaginaInicial("invitado")
        }

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
