package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.tierno.mohamed.aa.mohabeatsiii.data.model.Tiempo
import es.tierno.mohamed.aa.mohabeatsiii.domaine.tiempoUseCase.GetTiempoUseCase
import kotlinx.coroutines.launch

class TiempoViewModel() : ViewModel() {
    var tiempo = MutableLiveData<Tiempo>()

    private val getTiempoUseCase = GetTiempoUseCase()

    fun onCreate() {
        viewModelScope.launch {
            val result = getTiempoUseCase()

            if(result != null){
                tiempo.postValue(result)
            }
        }
    }
}