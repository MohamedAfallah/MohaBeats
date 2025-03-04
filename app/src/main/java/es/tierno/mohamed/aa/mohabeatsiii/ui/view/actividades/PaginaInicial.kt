package es.tierno.mohamed.aa.mohabeatsiii.ui.view.actividades

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityPaginaInicialBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.CancionesFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.FavoritosFrag
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.PerfilFrag


@AndroidEntryPoint
class PaginaInicial : AppCompatActivity() {
    private lateinit var binding: ActivityPaginaInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaginaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragContainer, CancionesFrag())
                .commit()
        }
        binding.bottomNav.setOnItemSelectedListener{menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home-> {
                    replaceFragment(CancionesFrag())
                    true
                }
                R.id.nav_favoritos -> {
                    replaceFragment(FavoritosFrag())
                    true
                }
                R.id.nav_perfil -> {
                    replaceFragment(PerfilFrag())
                    true
                }
                else -> false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        // Usar binding para obtener el contenedor y reemplazar el fragmento
        supportFragmentManager.beginTransaction()
            .replace(binding.fragContainer.id, fragment)
            .commit()
    }
}