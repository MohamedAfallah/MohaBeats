package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.GetUsuarioPorIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.ModificarUsuarioUseCase

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val getUsuarioUseCase: GetUsuarioPorIdUseCase,
    private val modificarUsuarioUseCase: ModificarUsuarioUseCase
) : ViewModel() {

    val usuario = MutableLiveData<Usuario?>()
    val isLoading = MutableLiveData<Boolean>()
    val saveSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun cargarUsuario(idUsuario: String?) {
        if (idUsuario.isNullOrBlank()) {
            errorMessage.postValue("ID de usuario no válido para cargar.")
            usuario.postValue(null)
            return
        }

        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val user = getUsuarioUseCase(idUsuario)
                usuario.postValue(user)
                Log.d("PerfilViewModel", "Usuario cargado: ${user?.usuario ?: "null"}")
            } catch (e: Exception) {
                errorMessage.postValue("Error al cargar usuario: ${e.message}")
                Log.e("PerfilViewModel", "Error al cargar usuario: ${e.message}", e)
                usuario.postValue(null)
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun guardarCambiosPerfil(
        newEmail: String,
        newPassword: String?,
        newNombreCompleto: String,
        newFechaNacimiento: String,
        newUsuarioDisplayName: String,
        newTelefono: String
    ) {
        isLoading.postValue(true)
        saveSuccess.postValue(false)
        errorMessage.postValue("")

        viewModelScope.launch {
            val currentUser = usuario.value
            val userId = currentUser?.id

            if (userId == null || currentUser == null) {
                errorMessage.postValue("No se pudo guardar el perfil: Datos de usuario no disponibles.")
                isLoading.postValue(false)
                return@launch
            }

            val oldEmail = currentUser.correo

            try {
                val result = modificarUsuarioUseCase(
                    userId = userId,
                    newEmail = newEmail,
                    oldEmail = oldEmail,
                    newPassword = newPassword,
                    newNombreCompleto = newNombreCompleto,
                    newFechaNacimiento = newFechaNacimiento,
                    newUsuarioDisplayName = newUsuarioDisplayName,
                    newTelefono = newTelefono
                )

                if (result.isSuccess) {
                    saveSuccess.postValue(true)
                    errorMessage.postValue("Perfil actualizado con éxito.")
                    Log.d("PerfilViewModel", "Usuario guardado con éxito: ${newUsuarioDisplayName}")
                    cargarUsuario(userId)
                } else {
                    saveSuccess.postValue(false)
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido al guardar el perfil."
                    errorMessage.postValue(errorMsg)
                    Log.e("PerfilViewModel", "Fallo al guardar usuario: $errorMsg", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                saveSuccess.postValue(false)
                errorMessage.postValue("Error inesperado al guardar usuario: ${e.message}")
                Log.e("PerfilViewModel", "Error inesperado al guardar usuario: ${e.message}", e)
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}