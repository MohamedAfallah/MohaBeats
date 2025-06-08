package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityPaginaInicialBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.bottom_sheet.BottomSheetInvitado
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.ChatBot
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.ContendorPostsPlaylist
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.DescargasFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.FavoritosFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.HistorialFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.InicioFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.MisPostsFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.PerfilFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views.ErrorDialogFragment
import es.tierno.mohamed.aa.mohabeatsiii.utils.ConexionInternet
import es.tierno.mohamed.aa.mohabeatsiii.utils.MenuOpciones

@AndroidEntryPoint
class PaginaInicial : AppCompatActivity() {

    private lateinit var binding: ActivityPaginaInicialBinding
    private var usuarioId: String = ""
    private var ultimoItemIdSeleccionado = R.id.nav_home
    private val fbAuth = FirebaseAuth.getInstance()
    private lateinit var lottieMenuHamburguesa: LottieAnimationView
    private var menuEstaAbierto = false
    private lateinit var animacionEntrada: Animation
    private lateinit var animacionSalida: Animation

    companion object {
        private const val INVITADO = "invitado"
        private const val TAG = "PaginaInicial"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioId = intent.getStringExtra("usuarioId") ?: INVITADO

        inicializarVistas()
        configurarAnimaciones()
        configurarListeners()
        configurarNavegacionInferior()
        configurarFragmentoInicial(savedInstanceState)
        actualizarTextoCerrarSesion()
    }

    override fun onResume() {
        super.onResume()
        actualizarTextoCerrarSesion()
    }

    private fun inicializarVistas() {
        lottieMenuHamburguesa = binding.lottieMenuHamburguesa
    }

    private fun configurarAnimaciones() {
        animacionEntrada = AnimationUtils.loadAnimation(this, R.anim.anim_entrada)
        animacionSalida = AnimationUtils.loadAnimation(this, R.anim.anim_salida)
    }

    private fun configurarListeners() {
        lottieMenuHamburguesa.setOnClickListener {
            alternarMenuFlotante()
        }

        binding.btnCloseMenuFlotante.setOnClickListener {
            alternarMenuFlotante()
        }

        binding.menuItemMisPosts.setOnClickListener { manejarClickMenuItem(MenuOpciones.MIS_POSTS) }
        binding.menuItemHistorial.setOnClickListener { manejarClickMenuItem(MenuOpciones.HISTORIAL) }
        binding.menuItemDescargas.setOnClickListener { manejarClickMenuItem(MenuOpciones.DESCARGAS) }
        binding.menuItemCerrarSesion.setOnClickListener { manejarClickMenuItem(MenuOpciones.CERRAR_SESION) }

        binding.reproductorMiniContainer.setOnClickListener {
            binding.reproductorMiniContainer.visibility = View.GONE
        }

        binding.overlayDimmer.setOnClickListener {
            alternarMenuFlotante()
        }
    }

    private fun configurarNavegacionInferior() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            val esInvitado = esUsuarioInvitado()
            val hayConexion = ConexionInternet.isNetworkAvailable(this)

            when (item.itemId) {
                R.id.nav_home -> {
                    abrirFragmento(InicioFrag().apply {
                        arguments = Bundle().apply { putString("idUsuario", usuarioId) }
                    })
                    ultimoItemIdSeleccionado = item.itemId
                    true
                }
                R.id.nav_chat -> {
                    abrirFragmento(ChatBot().apply {
                        arguments = Bundle().apply { putString("idUsuario", usuarioId) }
                    })
                    ultimoItemIdSeleccionado = item.itemId
                    true
                }
                R.id.nav_favoritos, R.id.nav_perfil, R.id.nav_playlist -> {
                    if (!hayConexion || esInvitado) {
                        mostrarRestriccion(hayConexion, esInvitado)
                        restablecerSeleccionNavegacionInferior()
                        false
                    } else {
                        val fragment = when (item.itemId) {
                            R.id.nav_favoritos -> FavoritosFrag().apply { arguments = Bundle().apply { putString("idUsuario", usuarioId) } }
                            R.id.nav_perfil -> PerfilFrag().apply { arguments = Bundle().apply { putString("idUsuario", usuarioId) } }
                            R.id.nav_playlist -> ContendorPostsPlaylist()
                            else -> InicioFrag()
                        }
                        abrirFragmento(fragment)
                        ultimoItemIdSeleccionado = item.itemId
                        true
                    }
                }
                else -> false
            }
        }
    }

    private fun configurarFragmentoInicial(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            abrirFragmento(InicioFrag().apply {
                arguments = Bundle().apply {
                    putString("idUsuario", usuarioId)
                }
            })
        }
    }

    private fun abrirFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragContainer.id, fragment)
            .commit()
    }

    private fun restablecerSeleccionNavegacionInferior() {
        binding.bottomNav.selectedItemId = ultimoItemIdSeleccionado
    }

    private fun esUsuarioInvitado() = usuarioId == INVITADO

    private fun actualizarTextoCerrarSesion() {
        binding.menuItemCerrarSesion.text = if (esUsuarioInvitado()) getString(R.string.lblIniciarSesion) else getString(R.string.menu_item_cerrar_sesion)
    }

    fun mostrarBottomSheetInvitado() {
        BottomSheetInvitado().show(supportFragmentManager, "BottomSheetInvitado")
    }

    private fun mostrarDialogoError(mensaje: String) {
        val dialog = ErrorDialogFragment.newInstance(mensaje)
        dialog.show(supportFragmentManager, "ErrorNetworkDialog")
    }

    private fun mostrarRestriccion(hayConexion: Boolean, esInvitado: Boolean) {
        if (!hayConexion) {
            mostrarDialogoError(getString(R.string.error_internet))
        } else if (esInvitado) {
            mostrarBottomSheetInvitado()
        }
    }

    private fun alternarMenuFlotante() {
        if (menuEstaAbierto) {
            cerrarMenu()
        } else {
            abrirMenu()
        }
    }

    private fun cerrarMenu() {
        binding.menuFlotanteContainer.startAnimation(animacionSalida)
        animacionSalida.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.menuFlotanteContainer.visibility = View.GONE
                binding.overlayDimmer.visibility = View.GONE
                lottieMenuHamburguesa.speed = -1f
                lottieMenuHamburguesa.playAnimation()
                menuEstaAbierto = false
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun abrirMenu() {
        binding.menuFlotanteContainer.visibility = View.VISIBLE
        binding.overlayDimmer.visibility = View.VISIBLE
        binding.menuFlotanteContainer.startAnimation(animacionEntrada)
        lottieMenuHamburguesa.speed = 1f
        lottieMenuHamburguesa.playAnimation()
        menuEstaAbierto = true
    }

    private fun manejarClickMenuItem(itemSeleccionado: MenuOpciones) {
        when (itemSeleccionado) {
            MenuOpciones.MIS_POSTS -> navegarSeguro(itemSeleccionado, true, true)
            MenuOpciones.HISTORIAL -> navegarSeguro(itemSeleccionado, true, true)
            MenuOpciones.DESCARGAS -> navegarSeguro(itemSeleccionado, false, false)
            MenuOpciones.CERRAR_SESION -> {
                if (esUsuarioInvitado()) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    fbAuth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
        alternarMenuFlotante()
    }

    private fun navegarSeguro(destino: MenuOpciones, requiereConexion: Boolean, requiereUsuario: Boolean) {
        val hayConexion = ConexionInternet.isNetworkAvailable(this)
        val esInvitado = esUsuarioInvitado()

        if (requiereConexion && !hayConexion) {
            mostrarDialogoError(getString(R.string.error_internet))
            return
        }

        when (destino) {
            MenuOpciones.MIS_POSTS -> {
                if (requiereUsuario && esInvitado) {
                    mostrarBottomSheetInvitado()
                    return
                }
                abrirFragmento(MisPostsFrag().apply {
                    arguments = Bundle().apply { putString("idUsuario", usuarioId) }
                })
            }
            MenuOpciones.HISTORIAL -> {
                if (requiereUsuario && esInvitado) {
                    mostrarBottomSheetInvitado()
                    return
                }
                abrirFragmento(HistorialFrag().apply {
                    arguments = Bundle().apply { putString("idUsuario", usuarioId) }
                })
            }
            MenuOpciones.DESCARGAS -> {
                abrirFragmento(DescargasFrag())
            }
            MenuOpciones.CERRAR_SESION -> {

            }
        }
    }

    override fun onBackPressed() {
        if (menuEstaAbierto) {
            alternarMenuFlotante()
        } else {
            super.onBackPressed()
        }
    }
}
