package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.CrearUsuarioUseCase
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.GetUsuarioPorIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val crearUsuarioUseCase: CrearUsuarioUseCase,
    private val getUsuarioPorIdUseCase: GetUsuarioPorIdUseCase,
) : ViewModel() {

    val usuario = MutableLiveData<Usuario?>()
    val idCreado = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    // Obtener usuario por ID
    fun cargarUsuarioPorId(id: String) {
        viewModelScope.launch {
            try {
                val user = getUsuarioPorIdUseCase(id)
                usuario.postValue(user)
            } catch (e: Exception) {
                error.postValue("Error al cargar usuario: ${e.message}")
            }
        }
    }

    // Crear un usuario nuevo
    fun crearUsuario(nuevoUsuario: Usuario) {
        viewModelScope.launch {
            try {
                val id = crearUsuarioUseCase(nuevoUsuario)
                if (id.isNotEmpty()) {
                    idCreado.postValue(id)
                } else {
                    error.postValue("No se pudo crear el usuario")
                }
            } catch (e: Exception) {
                error.postValue("Error al crear usuario: ${e.message}")
            }
        }
    }
}
