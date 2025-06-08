package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.historial.GetHistorialUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val getHistorialUseCase: GetHistorialUseCase,
    private val getCancionUseCase: GetCancionUseCase
) : ViewModel() {

    val musicaHistorial = MutableLiveData<List<Musica>>()

    fun onCreate(idUsuario: String) {
        viewModelScope.launch {
            val historialIds = getHistorialUseCase(idUsuario)
            val canciones = historialIds?.mapNotNull { idCancion ->
                getCancionUseCase(idCancion)
            }?.reversed() ?: emptyList()
            musicaHistorial.postValue(canciones)
        }
    }
}
