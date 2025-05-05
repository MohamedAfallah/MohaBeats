package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.GetRespuestaChatUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getRespuestaChatUseCase: GetRespuestaChatUseCase
) : ViewModel() {

    var respuesta = MutableLiveData<String>()

    fun obtenerRespuesta(mensajeUsuario: String) {
        viewModelScope.launch {
            val result = getRespuestaChatUseCase(mensajeUsuario)

            if (result != null) {
                respuesta.postValue(result)
            } else {
                respuesta.postValue("Error al obtener respuesta")
            }
        }
    }
}