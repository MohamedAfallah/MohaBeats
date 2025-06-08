package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.vm_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.posts.CrearPostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class dialogCrearPostViewModel @Inject constructor(
    private val crearPostUseCase: CrearPostUseCase
) : ViewModel() {

    private val _postCreationStatus = MutableStateFlow<Boolean?>(null)
    val postCreationStatus: StateFlow<Boolean?> = _postCreationStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun createPost(idUsuario: String, comentario: String, idPlayList: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _postCreationStatus.value = null
            try {
                crearPostUseCase(idUsuario, comentario, idPlayList)
                _postCreationStatus.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al crear el post."
                _postCreationStatus.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetStatus() {
        _postCreationStatus.value = null
        _error.value = null
    }
}