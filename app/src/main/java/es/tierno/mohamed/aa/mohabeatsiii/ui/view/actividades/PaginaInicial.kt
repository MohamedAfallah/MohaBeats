package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityPaginaInicialBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.bottom_sheet.BottomSheetInvitado
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.ChatBot
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.FavoritosFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.InicioFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.PerfilFrag
import android.util.Log // Asegúrate de tener esta importación

@AndroidEntryPoint
class PaginaInicial : AppCompatActivity() {

    private lateinit var binding: ActivityPaginaInicialBinding
    private var usuarioId: String = ""
    private var lastValidItemId = R.id.nav_home

    private lateinit var lottieMenuHamburguesa: LottieAnimationView
    private var isMenuOpen = false

    private lateinit var slideInAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    private var colorIconoAbierto: Int = 0
    private var colorIconoCerrado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // AÑADE ESTA LÍNEA AQUÍ
        Log.d("MenuDebug", "Visibilidad inicial de overlay_dimmer: ${if (binding.overlayDimmer.visibility == View.VISIBLE) "VISIBLE" else "GONE"}")

        usuarioId = intent.getStringExtra("usuarioId") ?: "invitado"

        lottieMenuHamburguesa = binding.lottieMenuHamburguesa

        slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_entrada)
        slideOutAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_salida)

        colorIconoAbierto = resources.getColor(R.color.colorPrincipal, theme)
        colorIconoCerrado = resources.getColor(R.color.colorDetalles, theme)

        lottieMenuHamburguesa.setOnClickListener {
            toggleMenuFlotante()
        }

        binding.btnCloseMenuFlotante.setOnClickListener {
            toggleMenuFlotante()
        }

        binding.menuItemMisPosts.setOnClickListener { handleMenuItemClick("Mis posts") }
        binding.menuItemHistorial.setOnClickListener { handleMenuItemClick("Historial") }
        binding.menuItemDescargas.setOnClickListener { handleMenuItemClick("Descargas") }
        binding.menuItemCerrarSesion.setOnClickListener { handleMenuItemClick("Cerrar Sesión") }

        if (savedInstanceState == null) {
            val inicioFrag = InicioFrag().apply {
                arguments = Bundle().apply {
                    putString("idUsuario", usuarioId)
                }
            }
            replaceFragment(inicioFrag)
        }

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            val esInvitado = usuarioId == "invitado"

            when (item.itemId) {
                R.id.nav_home -> {
                    val inicioFrag = InicioFrag().apply {
                        arguments = Bundle().apply {
                            putString("idUsuario", usuarioId)
                        }
                    }
                    replaceFragment(inicioFrag)
                    lastValidItemId = item.itemId
                    true
                }
                R.id.nav_favoritos -> {
                    if (esInvitado) {
                        mostrarBottomSheetInvitado()
                        binding.bottomNav.menu.findItem(lastValidItemId).isChecked = true
                        false
                    } else {
                        val fragment = FavoritosFrag().apply {
                            arguments = Bundle().apply {
                                putString("idUsuario", usuarioId)
                            }
                        }
                        replaceFragment(fragment)
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
                        val perfilFrag = PerfilFrag().apply {
                            arguments = Bundle().apply {
                                putString("idUsuario", usuarioId)
                            }
                        }
                        replaceFragment(perfilFrag)
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
                        replaceFragment(ChatBot())
                        lastValidItemId = item.itemId
                        true
                    }
                }
                else -> false
            }
        }

        binding.reproductorMiniContainer.setOnClickListener {
            binding.reproductorMiniContainer.visibility = View.GONE
        }

        lottieMenuHamburguesa.addLottieOnCompositionLoadedListener {
            applyLottieColor(colorIconoCerrado)
        }

        binding.overlayDimmer.setOnClickListener {
            Log.d("MenuDebug", "Click en overlay_dimmer detectado.")
            toggleMenuFlotante()
        }

        if (usuarioId == "invitado") {
            binding.menuItemCerrarSesion.text = "Iniciar Sesión"
        } else {
            binding.menuItemCerrarSesion.text = "Cerrar Sesión"
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragContainer.id, fragment)
            .commit()
    }

    fun mostrarBottomSheetInvitado() {
        val bottomSheet = BottomSheetInvitado()
        bottomSheet.show(supportFragmentManager, "BottomSheetInvitado")
    }

    private fun applyLottieColor(color: Int) {
        lottieMenuHamburguesa.addValueCallback(
            KeyPath("**", "Fill 1"),
            LottieProperty.COLOR,
            LottieValueCallback(color)
        )
        lottieMenuHamburguesa.invalidate()
    }

    private fun toggleMenuFlotante() {
        if (isMenuOpen) {
            Log.d("MenuDebug", "Cerrando menú. overlay_dimmer VISIBLE. isMenuOpen: $isMenuOpen")
            binding.menuFlotanteContainer.startAnimation(slideOutAnimation)
            slideOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    binding.menuFlotanteContainer.visibility = View.GONE
                    binding.overlayDimmer.visibility = View.GONE
                    Log.d("MenuDebug", "Menú cerrado. overlay_dimmer GONE. isMenuOpen: $isMenuOpen")
                    lottieMenuHamburguesa.speed = -1f
                    lottieMenuHamburguesa.playAnimation()
                    applyLottieColor(colorIconoCerrado)
                    isMenuOpen = false
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        } else {
            Log.d("MenuDebug", "Abriendo menú. overlay_dimmer GONE. isMenuOpen: $isMenuOpen")
            binding.menuFlotanteContainer.visibility = View.VISIBLE
            binding.overlayDimmer.visibility = View.VISIBLE
            Log.d("MenuDebug", "Menú abierto. overlay_dimmer VISIBLE. isMenuOpen: $isMenuOpen")
            binding.menuFlotanteContainer.startAnimation(slideInAnimation)
            lottieMenuHamburguesa.speed = 1f
            lottieMenuHamburguesa.playAnimation()
            applyLottieColor(colorIconoAbierto)
            isMenuOpen = true
        }
    }

    private fun handleMenuItemClick(selectedItem: String) {
        when (selectedItem) {
            "Mis posts" -> {
                if (usuarioId == "invitado") {
                    mostrarBottomSheetInvitado()
                } else {
                    Toast.makeText(this, "Navegando a Mis posts", Toast.LENGTH_SHORT).show()
                }
            }
            "Historial" -> {
                if (usuarioId == "invitado") {
                    mostrarBottomSheetInvitado()
                } else {
                    Toast.makeText(this, "Navegando a Historial", Toast.LENGTH_SHORT).show()
                }
            }
            "Descargas" -> {
                if (usuarioId == "invitado") {
                    mostrarBottomSheetInvitado()
                } else {
                    Toast.makeText(this, "Navegando a Descargas", Toast.LENGTH_SHORT).show()
                }
            }
            "Cerrar Sesión" -> {
                if (usuarioId == "invitado") {
                    Toast.makeText(this, "Redirigiendo para iniciar sesión...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Cerrando Sesión...", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        toggleMenuFlotante()
    }

    override fun onBackPressed() {
        if (isMenuOpen) {
            toggleMenuFlotante()
        } else {
            super.onBackPressed()
        }
    }
}