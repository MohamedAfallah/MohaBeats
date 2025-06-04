package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.BuscarCancionesUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesPorGeneroUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.AnadirCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.EliminarCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.GetFavoritosUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultadoBusquedaViewModel @Inject constructor(
    private val getCancionesPorGeneroUseCase: GetCancionesPorGeneroUseCase,
    private val buscarCancionesUseCase: BuscarCancionesUseCase,
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val anadirCancionUseCase: AnadirCancionUseCase,
    private val eliminarCancionUseCase: EliminarCancionUseCase
) : ViewModel() {

    private val _canciones = MutableLiveData<List<Musica>>()
    val canciones: LiveData<List<Musica>> = _canciones

    val favoritasIds = MutableLiveData<Set<String>>(emptySet())

    private var currentUserId: String = ""

    fun onCreate(idUsuario: String) {
        currentUserId = idUsuario
        cargarFavoritos(idUsuario)
    }

    fun cargarCanciones(generoId: String) {
        viewModelScope.launch {
            val lista = getCancionesPorGeneroUseCase(generoId)
            _canciones.postValue(lista)
            cargarFavoritos(currentUserId)
        }
    }

    fun buscarCanciones(query: String) {
        viewModelScope.launch {
            val lista = buscarCancionesUseCase(query)
            _canciones.postValue(lista)
            cargarFavoritos(currentUserId)
        }
    }

    fun limpiarResultados() {
        _canciones.postValue(emptyList())
    }

    private fun cargarFavoritos(idUsuario: String) {
        viewModelScope.launch {
            if (idUsuario.isNotEmpty() && idUsuario != "invitado") {
                val ids = getFavoritosUseCase(idUsuario)?.toSet() ?: emptySet()
                favoritasIds.postValue(ids)
            } else {
                favoritasIds.postValue(emptySet())
            }
        }
    }

    fun anadirAFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            anadirCancionUseCase(idUsuario, idCancion)
            cargarFavoritos(idUsuario)
        }
    }

    fun eliminarDeFavoritos(idUsuario: String, idCancion: String) {
        viewModelScope.launch {
            eliminarCancionUseCase(idUsuario, idCancion)
            cargarFavoritos(idUsuario)
        }
    }
}
