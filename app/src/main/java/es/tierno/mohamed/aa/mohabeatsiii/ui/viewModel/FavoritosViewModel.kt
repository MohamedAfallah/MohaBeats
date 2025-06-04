package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.AnadirCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.EliminarCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.GetFavoritosUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val getFavoritosUseCase: GetFavoritosUseCase,
    private val anadirCancionUseCase: AnadirCancionUseCase,
    private val eliminarCancionUseCase: EliminarCancionUseCase,
    private val getCancionUseCase: GetCancionUseCase
): ViewModel() {
    val musicaFavorita = MutableLiveData<List<Musica>>()

    fun onCreate(id: String) {
        viewModelScope.launch {
            val favoritosIds = getFavoritosUseCase(id)
            val canciones = favoritosIds?.mapNotNull { idCancion ->
                getCancionUseCase(idCancion)
            } ?: emptyList()
            musicaFavorita.postValue(canciones)
        }
    }

    fun anadirAFavoritos(id: String, idCancion: String) {
        viewModelScope.launch {
            anadirCancionUseCase(id, idCancion)
            onCreate(id) // recarga la lista
        }
    }

    fun eliminarDeFavoritos(id: String, idCancion: String) {
        viewModelScope.launch {
            eliminarCancionUseCase(id, idCancion)
            onCreate(id) // recarga la lista
        }
    }
}
