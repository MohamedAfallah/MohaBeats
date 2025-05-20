package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.chat_bot.Mensaje
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.GetRespuestaChatUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenAiViewModel @Inject constructor(
    private val getRespuestaChatUseCase: GetRespuestaChatUseCase
) : ViewModel() {
    val respuesta = MutableLiveData<List<Mensaje>>()
    val error = MutableLiveData<String>()

    fun onCreate(mensaje: String) {
        viewModelScope.launch {
            try {
                val result = getRespuestaChatUseCase.invoke(mensaje)
                if (!result.isNullOrEmpty()) {
                    respuesta.postValue(result)
                } else {
                    error.postValue("No se recibió ninguna respuesta.")
                }
            } catch (e: Exception) {
                error.postValue("Error al conectar con el servicio: ${e.message}")
            }
        }
    }
}
