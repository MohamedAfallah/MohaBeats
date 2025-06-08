package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.CrearUsuarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CrearCuentaViewModel @Inject constructor(
    private val crearUsuarioUseCase: CrearUsuarioUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _idCreado = MutableStateFlow<String?>(null)
    val idCreado: StateFlow<String?> = _idCreado.asStateFlow()

    private val _emailVerificado = MutableStateFlow(false)
    val emailVerificado: StateFlow<Boolean> = _emailVerificado.asStateFlow()

    fun crearUsuario(usuario: Usuario) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val uid = crearUsuarioUseCase.invoke(usuario)
                if (uid.isNotEmpty()) {
                    _idCreado.value = uid
                    auth.currentUser?.sendEmailVerification()?.await()
                } else {
                    _error.value = "Error al crear usuario o guardar datos. Verifica tus credenciales o conexión."
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al crear el usuario."
            } finally {
                _loading.value = false
            }
        }
    }

    fun checkEmailVerified() {
        viewModelScope.launch {
            val user: FirebaseUser? = auth.currentUser
            try {
                user?.reload()?.await()
                _emailVerificado.value = user?.isEmailVerified ?: false
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al verificar el estado del correo: ${e.message}"
                _emailVerificado.value = false
            }
        }
    }
}