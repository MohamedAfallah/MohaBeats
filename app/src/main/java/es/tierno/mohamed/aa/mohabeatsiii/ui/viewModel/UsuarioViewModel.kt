package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.GetUsuariosUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor (
    private val getUsuariosUseCase: GetUsuariosUseCase
): ViewModel() {
    val usuarios = MutableLiveData<List<Usuario>>()

    fun onCreate(){
        viewModelScope.launch{
            val result = getUsuariosUseCase.invoke()

            if(!result.isNullOrEmpty()){
                usuarios.postValue(result)
            }
        }
    }
}