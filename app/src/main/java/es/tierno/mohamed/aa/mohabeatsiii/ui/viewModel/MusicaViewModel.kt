package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicaViewModel @Inject constructor (
    private val getCancionesUseCase: GetCancionesUseCase
) : ViewModel() {

    private var todasCanciones = listOf<Musica>()
    private val paginaSize = 10
    private var paginaActual = 0

    val musicaPaginada = MutableLiveData<List<Musica>>()

    fun onCreate() {
        viewModelScope.launch {
            todasCanciones = getCancionesUseCase() // Trae, por ejemplo, 50 canciones
            paginaActual = 0
            cargarPagina()
        }
    }

    fun cargarPagina() {
        val fromIndex = paginaActual * paginaSize
        val toIndex = (fromIndex + paginaSize).coerceAtMost(todasCanciones.size)
        if (fromIndex < toIndex) {
            val pagina = todasCanciones.subList(0, toIndex)
            musicaPaginada.postValue(pagina)
            paginaActual++
        }
    }
}
