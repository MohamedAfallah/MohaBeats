package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.GetCancionesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicaViewModel @Inject constructor (
    private val getCancionesUseCase: GetCancionesUseCase
) : ViewModel(){
    val musica = MutableLiveData<List<Musica>>()

    fun onCreate(){

    }
}