package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesPorGeneroUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultadoBusquedaViewModel @Inject  constructor(
    private val getCancionesPorGeneroUseCase: GetCancionesPorGeneroUseCase
) : ViewModel(){
    private val _canciones = MutableLiveData<List<Musica>>()
    val canciones: LiveData<List<Musica>> = _canciones

    fun cargarCanciones(generoId: String) {
        viewModelScope.launch {
            val lista = getCancionesPorGeneroUseCase(generoId)
            _canciones.postValue(lista)
        }
    }
}