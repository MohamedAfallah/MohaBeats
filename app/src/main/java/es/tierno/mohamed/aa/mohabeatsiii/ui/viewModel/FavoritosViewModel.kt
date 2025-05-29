package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.favoritos.GetFavoritosUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val getFavoritosUseCase: GetFavoritosUseCase
): ViewModel() {
    val musicaFavorita = MutableLiveData<List<Musica>>()

    fun onCreate(id : Int){
        viewModelScope.launch{


        }
    }
}