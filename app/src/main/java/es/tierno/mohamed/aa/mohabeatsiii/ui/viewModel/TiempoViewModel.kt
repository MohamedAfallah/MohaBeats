package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.tierno.mohamed.aa.mohabeatsiii.data.model.TiempoModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.GetTiempoUseCase
import kotlinx.coroutines.launch



class TiempoViewModel() : ViewModel() {
    var tiempo = MutableLiveData<TiempoModel>()

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