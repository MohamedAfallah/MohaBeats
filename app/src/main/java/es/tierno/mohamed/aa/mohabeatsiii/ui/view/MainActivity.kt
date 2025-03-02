package es.tierno.mohamed.aa.mohabeatsiii.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ActivityMainBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.TiempoViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val tiempoViewModel : TiempoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tiempoViewModel.onCreate()

        tiempoViewModel.tiempo.observe(this, Observer{
            val resultado = tiempoViewModel.tiempo.value?.tiempoHoy.toString()
            binding.lblTiempo.text = "Hoy Hace un Día\n" + dividirString(resultado, "=", ".")

        })
    }

    fun dividirString(input: String, inicio: String, fin: String): String? {
        val startIndex = input.indexOf(inicio)
        val endIndex = input.indexOf(fin)

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            input.substring(startIndex + inicio.length, endIndex).trim() // trim() para eliminar espacios extras
        } else {
            null // Si no se encuentra, devuelve null
        }
    }
}