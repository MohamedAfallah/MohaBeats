package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas.EliminarDescarga
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas.GetTodasLasCancionesDescargasUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.descargas.InsertarUnaDescarga
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescargasViewModel @Inject constructor(
    private val getTodasLasCancionesDescargasUseCase: GetTodasLasCancionesDescargasUseCase,
    private val insertarUnaDescarga: InsertarUnaDescarga,
    private val eliminarDescarga: EliminarDescarga
) : ViewModel() {

    private val _cancionesDescargadas = MutableLiveData<List<Musica>>()
    val cancionesDescargadas: LiveData<List<Musica>> = _cancionesDescargadas

    init {
        obtenerTodasLasCancionesDescargadas()
    }

    private fun obtenerTodasLasCancionesDescargadas() {
        viewModelScope.launch {
            try {
                val canciones = getTodasLasCancionesDescargasUseCase.invoke()
                _cancionesDescargadas.postValue(canciones)
            } catch (e: Exception) {
            }
        }
    }

    fun descargarCancion(musica: Musica) {
        viewModelScope.launch {
            try {
                insertarUnaDescarga.invoke(musica)
            } catch (e: Exception) {
            }
        }
    }

    fun eliminarCancion(cancionId: String) {
        viewModelScope.launch {
            try {
                eliminarDescarga.invoke(cancionId)
            } catch (e: Exception) {
            }
        }
    }
}