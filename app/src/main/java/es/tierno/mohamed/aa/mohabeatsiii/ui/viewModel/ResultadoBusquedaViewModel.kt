package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.BuscarCancionesUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesPorGeneroUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultadoBusquedaViewModel @Inject constructor(
    private val getCancionesPorGeneroUseCase: GetCancionesPorGeneroUseCase,
    private val buscarCancionesUseCase: BuscarCancionesUseCase  // Inyectar el caso de uso para buscar
) : ViewModel() {

    private val _canciones = MutableLiveData<List<Musica>>()
    val canciones: LiveData<List<Musica>> = _canciones

    // Cargar canciones por género (como tienes ya)
    fun cargarCanciones(generoId: String) {
        viewModelScope.launch {
            val lista = getCancionesPorGeneroUseCase(generoId)
            _canciones.postValue(lista)
        }
    }

    // NUEVO: buscar canciones por texto (búsqueda en tiempo real)
    fun buscarCanciones(query: String) {
        viewModelScope.launch {
            val lista = buscarCancionesUseCase(query)
            _canciones.postValue(lista)
        }
    }

    // Opcional: limpiar resultados si el texto es muy corto o vacío
    fun limpiarResultados() {
        _canciones.postValue(emptyList())
    }
}
