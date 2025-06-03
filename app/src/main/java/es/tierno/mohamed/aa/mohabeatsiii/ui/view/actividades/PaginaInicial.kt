package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityPaginaInicialBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.bottom_sheet.BottomSheetInvitado
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.*

@AndroidEntryPoint
class PaginaInicial : AppCompatActivity() {

    private lateinit var binding: ActivityPaginaInicialBinding
    private var usuarioId: String = ""  // Cambiado a String para poder usar "invitado"
    private var lastValidItemId = R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioId = intent.getStringExtra("usuarioId") ?: "invitado"

        if (savedInstanceState == null) {
            replaceFragment(InicioFrag())
        }

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            val esInvitado = usuarioId == "invitado"

            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(InicioFrag())
                    lastValidItemId = item.itemId
                    true
                }
                R.id.nav_favoritos -> {
                    if (esInvitado) {
                        mostrarBottomSheetInvitado()
                        binding.bottomNav.menu.findItem(lastValidItemId).isChecked = true
                        false
                    } else {
                        replaceFragment(FavoritosFrag())
                        lastValidItemId = item.itemId
                        true
                    }
                }
                R.id.nav_perfil -> {
                    if (esInvitado) {
                        mostrarBottomSheetInvitado()
                        binding.bottomNav.menu.findItem(lastValidItemId).isChecked = true
                        false
                    } else {
                        replaceFragment(PerfilFrag())
                        lastValidItemId = item.itemId
                        true
                    }
                }
                R.id.nav_chat -> {
                    replaceFragment(ChatBot())
                    lastValidItemId = item.itemId
                    true
                }
                R.id.nav_playlist -> {
                    if (esInvitado) {
                        mostrarBottomSheetInvitado()
                        binding.bottomNav.menu.findItem(lastValidItemId).isChecked = true
                        false
                    } else {
                        replaceFragment(ChatBot()) // Aquí pones la playlist real si la tienes
                        lastValidItemId = item.itemId
                        true
                    }
                }
                else -> false
            }
        }

        // Ejemplo básico de ocultar el reproductor al hacer clic
        binding.reproductorMiniContainer.setOnClickListener {
            binding.reproductorMiniContainer.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragContainer.id, fragment)
            .commit()
    }

    private fun mostrarBottomSheetInvitado() {
        // Aquí muestras el BottomSheet para invitado
        // Por ejemplo:
        val bottomSheet = BottomSheetInvitado()
        bottomSheet.show(supportFragmentManager, "BottomSheetInvitado")
    }
}
