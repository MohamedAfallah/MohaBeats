package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Importa esto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.usuario.CrearUsuarioUseCase // Importa tu Use Case
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch // Importa esto
import javax.inject.Inject // Importa esto

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

        viewModelScope.launch { // Usamos un Coroutine para llamar al Use Case (que es suspend)
            try {
                val uid = crearUsuarioUseCase.invoke(usuario) // ¡Llamamos al Use Case!
                if (uid.isNotEmpty()) {
                    _idCreado.value = uid
                    // El email de verificación ya se envía dentro de FbAuth por el Use Case
                } else {
                    // Esto significa que Auth falló, o Firestore falló, o no se pudo obtener el UID
                    _error.value = "Error al crear usuario o guardar datos. Verifica tus credenciales o conexión."
                }
            } catch (e: Exception) {
                // Captura cualquier excepción que no haya sido manejada por el Use Case
                _error.value = e.message ?: "Error desconocido al crear el usuario."
            } finally {
                _loading.value = false
            }
        }
    }

    fun checkEmailVerified() {
        val user: FirebaseUser? = auth.currentUser
        user?.reload()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _emailVerificado.value = user.isEmailVerified
            } else {
                _error.value = "Error verificando el estado del correo"
            }
        }
    }
}

