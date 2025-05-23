package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityPaginaInicialBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.InicioFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.ChatBot
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.FavoritosFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.PerfilFrag


@AndroidEntryPoint
class PaginaInicial : AppCompatActivity() {
    private lateinit var binding: ActivityPaginaInicialBinding
    var usuarioId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioId = intent.getIntExtra("usuarioId", -1)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragContainer, InicioFrag())
                .commit()
        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.nav_home -> InicioFrag()
                R.id.nav_favoritos -> FavoritosFrag()
                R.id.nav_perfil -> PerfilFrag()
                R.id.nav_chat -> ChatBot()
                R.id.nav_playlist -> ChatBot()
                else -> null
            }

            fragment?.let {
                val bundle = Bundle()
                bundle.putInt("usuarioId", usuarioId)
                it.arguments = bundle
                replaceFragment(it)
            }

            true
        }

    }

    //Remplazar el fragmento con otro que se ha seleccionado
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragContainer.id, fragment)
            .commit()
    }
}